package org.sample.students.dal.mysql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.sample.students.appcontext.config.MySQLConfig;

public class BaseDAO {

    protected BasicDataSource dataSource;

    public BaseDAO(MySQLConfig mySQLConfig) {
        Datasource mysqlDatasource = new Datasource(mySQLConfig);
        this.dataSource = mysqlDatasource.getDatasource();
    }
}
