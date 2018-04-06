package com.keruyun.fintech.commons.mybatis.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/7/14 17:46
 */
public class SqlBuilder {
    private static final Map<Class,TableEntity> classTableMap = new ConcurrentHashMap<>();//实体和表的对应关系
    private static final String COLUMN_ID = "id";//默认id字段是主键
    private static final char DB_NAME_LINK = '_';//数据库命名连接符
    private static final String SQL_VALUE_PREFIX = "#{";
    private static final char SQL_VALUE_SUBFIX = '}';
    private static final char SQL_COLUMN_LINK = ',';
    private static final char SQL_COLUMN_SET_LINK = '=';
    private static final String INSERT = "insert into %s(%s) values(%s)";
    private static final String UPDATE = "update %s set %s where %s";
    private static final String SELECT = "select %s from %s where %s";
    private static final Logger log = LoggerFactory.getLogger(SqlBuilder.class);

    /**
     * 生成mybatis新增sql
     * @param entityClass
     * @return
     */
    public static String classToInsertSql(Class entityClass) {
        TableEntity tableEntity = getTableEntity(entityClass);
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        String tableName = tableEntity.getTableName();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntitys();
        //生成列和值部分
        boolean hasColumn = false;
        for (ColumnEntity columnEntity : columnEntities) {
            if (!insertable(columnEntity)) {
                continue;
            }
            hasColumn = true;
            String columnName = columnEntity.getColumnName();
            columns.append(SQL_COLUMN_LINK).append(columnName);
            placeholders.append(SQL_COLUMN_LINK).append(SQL_VALUE_PREFIX)
                    .append(columnEntity.getField().getName()).append(SQL_VALUE_SUBFIX);
        }
        //生成sql
        if (hasColumn) {
            columns.deleteCharAt(0);
            placeholders.deleteCharAt(0);
        }

        return String.format(INSERT, tableName, columns, placeholders);
    }

    /**
     * 生成mybatis新增sql
     * @param entity
     * @return
     */
    public static String entityToInsertSql(Object entity) {
        if (entity == Class.class) {
            return classToInsertSql((Class)entity);
        }
        TableEntity tableEntity = getTableEntity(entity.getClass());
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        String tableName = tableEntity.getTableName();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntitys();
        //生成列和值部分
        boolean hasColumn = false;
        try {
            for (ColumnEntity columnEntity : columnEntities) {
                if (!insertable(columnEntity)) {
                    continue;
                }
                Object value = columnEntity.getReadMethod().invoke(entity);
                if (null == value) {
                    continue;
                }
                hasColumn = true;
                String columnName = columnEntity.getColumnName();
                columns.append(SQL_COLUMN_LINK).append(columnName);
                placeholders.append(SQL_COLUMN_LINK).append(SQL_VALUE_PREFIX)
                        .append(columnEntity.getField().getName()).append(SQL_VALUE_SUBFIX);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        //生成sql
        if (hasColumn) {
            columns.deleteCharAt(0);
            placeholders.deleteCharAt(0);
        }

        return String.format(INSERT, tableName, columns, placeholders);
    }

    /**
     * 生成mybatis更新sql
     * @param entityClass
     * @return
     */
    public static String classToUpdateSql(Class entityClass) {
        TableEntity tableEntity = getTableEntity(entityClass);
        StringBuilder placeholders = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();

        String tableName = tableEntity.getTableName();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntitys();
        //生成列和值部分
        boolean hasColumn = false;
        ColumnEntity pkColumnEntity = null;
        String columnName;
        for (ColumnEntity columnEntity : columnEntities) {
            if (columnEntity.isPrimaryKey()) {
                pkColumnEntity = columnEntity;
                continue;
            }
            if (!updatable(columnEntity)) {
                continue;
            }
            hasColumn = true;
            columnName = columnEntity.getColumnName();
            placeholders.append(SQL_COLUMN_LINK).append(columnName)
                    .append(SQL_COLUMN_SET_LINK).append(SQL_VALUE_PREFIX)
                    .append(columnEntity.getField().getName()).append(SQL_VALUE_SUBFIX);
        }
        columnName = pkColumnEntity.getColumnName();
        whereClause.append(columnName).append(SQL_COLUMN_SET_LINK)
                .append(SQL_VALUE_PREFIX).append(pkColumnEntity.getField().getName()).append(SQL_VALUE_SUBFIX);
        //生成sql
        if (hasColumn) {
            placeholders.deleteCharAt(0);
        }

        return String.format(UPDATE, tableName, placeholders, whereClause);
    }

    /**
     * 生成mybatis查询sql
     * @param entityClass
     * @return
     */
    public static String classToSelectSql(Class entityClass) {
        TableEntity tableEntity = getTableEntity(entityClass);
        StringBuilder columns = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();

        String tableName = tableEntity.getTableName();
        List<ColumnEntity> columnEntities = tableEntity.getColumnEntitys();
        //生成列和值部分
        boolean hasColumn = false;
        ColumnEntity pkColumnEntity = null;
        String columnName;
        for (ColumnEntity columnEntity : columnEntities) {
            if (columnEntity.isPrimaryKey()) {
                pkColumnEntity = columnEntity;
                continue;
            }
            hasColumn = true;
            columnName = columnEntity.getColumnName();
            columns.append(SQL_COLUMN_LINK).append(columnName);
        }
        columnName = pkColumnEntity.getColumnName();
        whereClause.append(columnName).append(SQL_COLUMN_SET_LINK)
                .append(SQL_VALUE_PREFIX).append(pkColumnEntity.getField().getName()).append(SQL_VALUE_SUBFIX);
        //生成sql
        if (hasColumn) {
            columns.deleteCharAt(0);
        }

        return String.format(SELECT, columns, tableName, whereClause);
    }

    private static TableEntity getTableEntity(Class entityClass) {
        TableEntity tableEntity = classTableMap.get(entityClass);
        if (null != tableEntity) {
            return tableEntity;
        }
        tableEntity = new TableEntity();
        tableEntity.setTableName(getTableName(entityClass));
        tableEntity.setColumnEntitys(getColumnEntitys(entityClass));
        classTableMap.put(entityClass,tableEntity);
        return tableEntity;
    }
    private static String getTableName(Class entityClass) {
        Table table = (Table)entityClass.getAnnotation(Table.class);
        if (null != table) {
            return table.name();
        }
        return javaName2DbName(entityClass.getSimpleName());
    }
    private static List<ColumnEntity> getColumnEntitys(Class entityClass) {
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(entityClass);
        List<ColumnEntity> columnEntities = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            Method readMethod = pd.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Field field;
            try {
                field = entityClass.getDeclaredField(pd.getName());
                Transient t = field.getAnnotation(Transient.class);
                if (null != t) {
                    continue;
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
                continue;
            }
            boolean isPrimaryKey;//是否为主键
            if (null != field.getAnnotation(Id.class)) {
                isPrimaryKey = true;
            } else {
                isPrimaryKey = COLUMN_ID.equalsIgnoreCase(pd.getName());
            }
            String columnName = javaName2DbName(pd.getName());
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setField(field);
            columnEntity.setReadMethod(readMethod);
            columnEntity.setColumnName(columnName);
            columnEntity.setPrimaryKey(isPrimaryKey);
            columnEntities.add(columnEntity);
        }
        return columnEntities;
    }

    /**
     * 是否可插入（有些字段由数据库维护，则不需要insert）
     * @param columnEntity
     * @return
     */
    private static boolean insertable(ColumnEntity columnEntity) {
        if (columnEntity.isPrimaryKey()) {
            return false;
        }
        Column column = columnEntity.getField().getAnnotation(Column.class);
        if (null == column) {
            return true;
        }
        return column.insertable();
    }
    /**
     * 是否可更新（有些字段由数据库维护，则不需要update）
     * @param columnEntity
     * @return
     */
    private static boolean updatable(ColumnEntity columnEntity) {
        if (columnEntity.isPrimaryKey()) {
            return false;
        }
        Column column = columnEntity.getField().getAnnotation(Column.class);
        if (null == column) {
            return true;
        }
        return column.updatable();
    }
    /**
     * Java命名转为数据库命名
     * 驼峰方式改为下划线连接方式
     * @param javaName
     * @return
     */
    private static String javaName2DbName(String javaName) {
        char[] chars = javaName.toCharArray();
        StringBuilder sb = new StringBuilder();
        char c = toLowerCase(chars[0]);
        sb.append(c);
        for (int i = 1,size = chars.length;i < size;i++) {
            c = chars[i];
            if (isUpperCase(c)) {
                sb.append(DB_NAME_LINK);
                c = (char)(c + 32);
            }
            sb.append(c);
        }
        return sb.toString();
    }
    /**
     * 转换为小写字母(是大写字母则进行转换，否则不转换)
     * @param c
     * @return
     */
    private static char toLowerCase(char c) {
        if (isUpperCase(c)) {
            //是大写字母
            return (char)(c + 32);
        }
        return c;
    }
    private static boolean isUpperCase(char c) {
        if (c <= 90 && c >= 65) {
            //是大写字母
            return true;
        }
        return false;
    }
}
