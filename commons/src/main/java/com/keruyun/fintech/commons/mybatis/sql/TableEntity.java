package com.keruyun.fintech.commons.mybatis.sql;

import lombok.Data;

import java.util.List;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/7/14 13:59
 */
@Data
public class TableEntity {
    private String tableName;//实体对应的数据库表名
    private List<ColumnEntity> columnEntitys;//数据库表中的字段定义
}
