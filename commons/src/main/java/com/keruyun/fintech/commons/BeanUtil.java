package com.keruyun.fintech.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * <p>Title: BeanUtil</p> <p>Description: com.wqb.test</p> <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: 客如云</p>
 *
 * @author dongt
 * @date 2016年3月21日
 */
@Slf4j
public final class BeanUtil {
    private static final String JAVA_LANG_PACKAGE = "java.lang";
    /**
     * @param bean  Bean对象
     * @param name  属性名称
     * @param value 值
     * @author PengYanan
     */
    public static Object copyProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.copyProperty(bean, name, value);
        } catch (IllegalAccessException e) {
            log.error("Bean对象反射时出现异常", e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            log.error("Bean对象中没有找到属性名为:{}", name, e);
        }
        return bean;
    }

    /**
     * 复制资源Bean中的属性值到目标Bean中
     *
     * @param targetObj 目标Bean
     * @param sourceObj 资源Bean
     */
    public static Object copyProperties(Object targetObj, Object sourceObj) {
        if (null == targetObj || null == sourceObj) {
            return targetObj;
        }
        try {
            BeanUtils.copyProperties(targetObj, sourceObj);
        } catch (Exception e) {
            log.error("Bean CopyProperties Exception", e);
        }
        return targetObj;
    }

    /**
     * 获取class本身及所有父类定义的所有属性(不重复)
     * @param clazz
     * @return
     */
    public static Collection<Field> getAllDeclaredFields(Class clazz) {
        if (String.class == clazz) {
            return Collections.EMPTY_LIST;
        }
        Class cls = clazz;
        HashMap<String,Field> fieldMap = new HashMap<>();
        String key;
        while (!cls.getName().startsWith(JAVA_LANG_PACKAGE)) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                key = field.getName();
                if (fieldMap.containsKey(key)) {
                    continue;
                }
                fieldMap.put(key,field);
            }
            cls = cls.getSuperclass();
        }
        return fieldMap.values();
    }

    /**
     * Bean转换为Map格式
     */
    @SuppressWarnings("all")
    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return null;
        }
        if (Map.class.isAssignableFrom(bean.getClass())) {
            return (Map<String, Object>) bean;
        }
        Collection<Field> fields = getAllDeclaredFields(bean.getClass());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(bean));
                }
            }
        } catch (Exception e) {
            log.error("exception:", e);
        }
        return map;
    }

    /**
     * Bean转换为Map格式 忽略NULL
     */
    @SuppressWarnings("all")
    public static Map<String, Object> toMapIgnoreNULL(Object bean) {
        if (bean == null) {
            return null;
        }
        if (Map.class.isAssignableFrom(bean.getClass())) {
            return (Map<String, Object>) bean;
        }
        Collection<Field> fields = getAllDeclaredFields(bean.getClass());
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers()) && field.get(bean)!=null) {
                    map.put(field.getName(), field.get(bean));
                }
            }
        } catch (Exception e) {
            log.error("exception:", e);
        }
        return map;
    }

    /**
     * 设置javabean中非static、非final的对象属性的值为null
     * @param bean
     */
    public static void setField2Null(Object bean) {
        try {
            Collection<Field> fields = BeanUtil.getAllDeclaredFields(bean.getClass());
            for (Field field : fields) {
                int modifier = field.getModifiers();
                if (Modifier.isStatic(modifier) || Modifier.isFinal(modifier)) {
                    continue;
                }
                if (Object.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    field.set(bean, null);
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    /**
     * 根据名称递归获取class中定义的字段
     * @param clazz
     * @param name
     * @return
     */
    public static Field getFieldRecursion(Class clazz,String name) {
        if (clazz.getName().startsWith(JAVA_LANG_PACKAGE) || StringUtils.isBlank(name)) {
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (name.equals(field.getName())) {
                return field;
            }
        }
        return BeanUtil.getFieldRecursion(clazz.getSuperclass(),name);
    }

    /**
     * 根据class中的属性名获取对应的json字段名，没有JsonProperty时，直接返回name
     * @param clazz
     * @param name
     * @return
     */
    public static String getJsonColumnName(Class clazz,String name) {
        Field field = BeanUtil.getFieldRecursion(clazz,name);
        if (null == field) {
            log.error("尝试获取json字段名出错,class={},feildName={}",clazz,name);
            return name;
        }
        return BeanUtil.getJsonColumnName(field,name);
    }
    /**
     * 根据属性Field获取对应的json字段名，没有JsonProperty时，直接返回fieldName
     * @param field
     * @param name
     * @return
     */
    public static String getJsonColumnName(Field field,String name) {
        if (null == field) {
            return name;
        }
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (null == jsonProperty) {
            return name;
        }
        String jsonName = jsonProperty.value();
        if (StringUtils.isBlank(jsonName)) {
            return name;
        }
        return jsonName;
    }

    /**
     * 获取对象中指定属性的值，屏蔽异常
     * @param obj
     * @param field
     * @return Object
     */
    public static Object getFieldValue(Object obj,Field field) {
        if (null == field) {
            return null;
        }
        Object value = null;
        try {
            field.setAccessible(true);
            value = field.get(obj);
        } catch (Exception e) {
            log.error("获取{}.{}的值出错,{}",obj.getClass(),field.getName(), e.getLocalizedMessage());
        }
        return value;
    }

    /**
     * 获取对象中指定属性的值，屏蔽异常
     * @param obj
     * @param fieldName
     * @return Object
     */
    public static Object getFieldValueByName(Object obj,String fieldName) {
        Field field = getFieldRecursion(obj.getClass(),fieldName);

        return getFieldValue(obj,field);
    }

    /**
     * 设置字符串值到对象的指定字段（会将字符串转换为目标字段的类型）
     * @param obj
     * @param field
     * @param value
     */
    public static void setFieldValue(Object obj,Field field,Object value) {
        try {
            Object objValue = convertToBaseType(value, field.getType());
            if (null == objValue) {
                return;
            }
            field.setAccessible(true);
            field.set(obj, objValue);
        } catch (Exception e) {
            log.error("设置数据失败，fieldName:{}，fieldValue:{}，{}",field.getName(),value, e.getLocalizedMessage());
        }
    }

    /**
     * 未知对象转基本类型值
     * @param value
     * @param clazz
     * @return Object
     */
    public static Object convertToBaseType(Object value, Class clazz){
        if (value == null || null == clazz) {
            return null;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            return value;
        }
        String s;
        if (value.getClass() == String.class) {
            s = (String)value;
        } else {
            s = "" + value;
        }
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        if (int.class == clazz || Integer.class == clazz) {
            if(NumberUtils.isNumber(s)){
                return NumberUtils.createInteger(s);
            }
            log.warn(s + " 不能转换为Integer");
            return null;
        }
        if (long.class == clazz || Long.class == clazz) {
            if(NumberUtils.isNumber(s)){
                return NumberUtils.createLong(s);
            }
            log.warn(s + " 不能转换为Long");
            return null;
        }
        if (BigDecimal.class == clazz) {
            return new BigDecimal(s);
        }
        if (Date.class == clazz) {
            long ms = parserTimeStrToMs(s);
            if (0 == ms) {
                log.warn(s + " 不能转换为Date");
            }
            return new Date(ms);
        }
        if (Timestamp.class == clazz) {
            long ms = parserTimeStrToMs(s);
            if (0 == ms) {
                log.warn(s + " 不能转换为Timestamp");
            }
            return new Timestamp(ms);
        }
        if (float.class == clazz || Float.class == clazz) {
            if(NumberUtils.isNumber(s)){
                return NumberUtils.createFloat(s);
            }
            log.warn(s + " 不能转换为Float");
            return null;
        }
        if (double.class == clazz || Double.class == clazz) {
            if(NumberUtils.isNumber(s)){
                return NumberUtils.createDouble(s);
            }
            log.warn(s + " 不能转换为Double");
            return null;
        }
        if (byte.class == clazz || Byte.class == clazz) {
            if(1 == s.length()){
                return NumberUtils.toByte(s);
            }
            log.warn(s + " 不能转换为Byte");
            return null;
        }
        if (short.class == clazz || Short.class == clazz) {
            if (NumberUtils.isNumber(s)) {
                return NumberUtils.toShort(s);
            }
            log.warn(s + " 不能转换为Short");
            return null;
        }
        if (char.class == clazz || Character.class == clazz) {
            if(1 == s.length()){
                return s.charAt(0);
            }
            log.warn(s + " 不能转换为Character");
            return null;
        }
        if (boolean.class == clazz || Boolean.class == clazz) {
            return Boolean.valueOf(s);
        }
        return value;
    }
    /**
     * 解析时间字符串为毫秒数
     * @param s
     * @return
     */
    public static long parserTimeStrToMs(String s) {
        if (StringUtils.isBlank(s)) {
            return 0;
        }
        if (NumberUtils.isNumber(s)) {
            return Long.parseLong(s);
        }
        return FormatUtils.parseDate(s,FormatUtils.LONG_DATE_FORMAT_STR).getTime();
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if(map == null) {
            return null;
        }
        Object obj = beanClass.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }

            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }

        return obj;
    }
}