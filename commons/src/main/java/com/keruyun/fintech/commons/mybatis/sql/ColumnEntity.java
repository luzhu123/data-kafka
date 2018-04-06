package com.keruyun.fintech.commons.mybatis.sql;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/7/14 14:53
 */
@Data
public class ColumnEntity {
    private Field field;//实体属性
    private Method readMethod;//实体属性的get方法
    private String columnName;//数据库字段名
    private boolean isPrimaryKey;//是否主键
}
