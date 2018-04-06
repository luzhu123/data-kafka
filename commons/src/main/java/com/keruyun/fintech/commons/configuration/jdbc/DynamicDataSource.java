package com.keruyun.fintech.commons.configuration.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 通过JdbcContextHolder动态获取数据源名称
 * @author shuwei
 * @version 1.0
 * @date 2015/5/11 10:08
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return JdbcContextHolder.getJdbcType();
    }
}
