package com.keruyun.fintech.commons;


import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shuw
 * @version 1.0
 * @date 2016/10/19 16:29
 */
public class Utils {
    private static final Pattern pattern = Pattern.compile("\"msgId\"\\s?:\\s?\"\\S+\"");//获取msgId的正则表达式
    public static final String KERY_PACKAGE_PRE = "com.keruyun";//客如云包头

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 从json字符串中提取msgId字段的值
     *
     * @param messageText
     * @return
     */
    public static String getMsgId(String messageText) {
        try {
            Matcher matcher = pattern.matcher(messageText);
            if (matcher.find()) {
                String msgId = matcher.group();
                int n = msgId.indexOf(':');
                msgId = msgId.substring(n + 1);
                n = msgId.indexOf('"');
                int start = n + 1;
                int end = msgId.length() - start;
                return msgId.substring(start, end);
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取异常所在的业务代码相关的信息
     *
     * @param e
     * @return
     */
    public static String getExceptionInfo(Throwable e) {
        return getExceptionInfo(e, KERY_PACKAGE_PRE, 0);
    }

    /**
     * 获取异常所在的业务代码相关的信息
     *
     * @param e
     * @return
     */
    public static String getExceptionInfo(Throwable e, String packageStr, int index) {
        if (e == null) {
            return "";
        }
        String errInfo = null;
        StackTraceElement[] ss = e.getStackTrace();
        int n = 0;
        if (ss != null && ss.length > 0) {
            for (StackTraceElement s : ss) {
                if (s == null) {
                    continue;
                }
                errInfo = s.toString();
                if (errInfo.contains(packageStr)) {
                    if (++n > index) {
                        break;
                    }
                }
            }
        }
        String message = e.getMessage();
        return e.getClass().getSimpleName() + (message == null ? "" : (":" + message)) + ":" + (errInfo == null ? "" : errInfo);
    }

//    /**
//     * 按字节截取字符串
//     * @param s
//     * @param byteCount 截取字节数
//     * @return
//     */
//    public static String subByteString(String s,int byteCount) {
//        //处理错误信息的长度，防止超长，影响正常业务
//        if (StringUtils.isEmpty(s)) {
//            return s;
//        }
//        byte[] data = s.getBytes(Constant.DEFAULT_CHARSET_OBJ);
//        if (data.length > byteCount) {
//            return new String(Arrays.copyOf(data, byteCount),Constant.DEFAULT_CHARSET_OBJ);
//        }
//        return s;
//    }

    /**
     * 截取字符串
     *
     * @param s
     * @param charCount 截取字符数
     * @return
     */
    public static String subString(String s, int charCount) {
        if (null == s || s.length() < charCount) {
            return s;
        }
        return s.substring(0, charCount);
    }

    /**
     * Collection为空
     *
     * @param collection
     * @return
     */
    public static boolean collectionIsEmpty(Collection collection) {
        if (collection == null) {
            return true;
        }
        if (collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Collection不空
     *
     * @param collection
     * @return
     */
    public static boolean collectionIsNotEmpty(Collection collection) {
        return !collectionIsEmpty(collection);
    }

    public static boolean mapIsEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 将对象转换为Map
     *
     * @param o
     * @return
     */
    public static Map objectToMap(Object o) {
        JSONObject jsonObject = JSONObject.fromObject(o);
        Map map = (HashMap) JSONObject.toBean(jsonObject, HashMap.class);
        if (map.get("commercialId") instanceof Integer) {
            map.put("commercialId", Long.valueOf((Integer) map.get("commercialId")));
        }
        return map;
    }

    /**
     * 获取请求Map
     *
     * @param o
     * @return
     */
    public static Map getRequestMap(Object o) {
        Map map = objectToMap(o);
        return map;
    }

    public static String getDefaultStr(String paramStr) {
        if (paramStr == null) {
            return "";
        }
        return paramStr;
    }

    /**
     * 将字符串转换为Long
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static Long strToLong(String str, Long defaultValue) {
        if (str == null || str.equals("")) {
            return defaultValue;
        }
        return Long.parseLong(str);
    }

    public static Long getLongValue(String str) {
        if (StringUtils.isNotBlank(str)) {
            return Long.valueOf(str);
        }
        return null;
    }
}
