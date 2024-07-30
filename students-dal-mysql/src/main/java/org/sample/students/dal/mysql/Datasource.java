package org.sample.students.dal.mysql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.sample.students.appcontext.config.MySQLConfig;

public class Datasource {
    private BasicDataSource datasource;
    public Datasource(MySQLConfig mysqlConfig) {
        this.setDatasource(mysqlConfig);
    }

    private void setDatasource(MySQLConfig mysqlConfig) {
        datasource = new BasicDataSource();
        datasource.setUrl(mysqlConfig.getUrl());
        datasource.setUsername(mysqlConfig.getUsername());
        datasource.setPassword(mysqlConfig.getPassword());

        String minIdle = mysqlConfig.getProperty("minIdle");
        String maxIdle = mysqlConfig.getProperty("maxIdle");
        String maxTotal = mysqlConfig.getProperty("maxTotal");

        datasource.setMinIdle(minIdle != null ? Integer.parseInt(minIdle) : 5);
        datasource.setMaxIdle(maxIdle != null ? Integer.parseInt(maxIdle) : 10);
        datasource.setMaxTotal(maxTotal != null ? Integer.parseInt(maxTotal) : 25);
    }

    public BasicDataSource getDatasource() {
        return this.datasource;
    }
}
