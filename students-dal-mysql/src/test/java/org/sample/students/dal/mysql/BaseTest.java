package org.sample.students.dal.mysql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.sample.students.appcontext.config.MySQLConfig;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.Statement;

public class BaseTest {
    protected MySQLConfig mySQLConfig;
    protected DAOFactory mysqlDAOFactory;
    protected Connection connection;

    @Before
    public void setup() throws Exception {
        mySQLConfig = new MySQLConfig("jdbc:mysql://localhost:3306/students_testing_db", "root", "students");
        mysqlDAOFactory = new DAOFactory(mySQLConfig);

        this.stopIfRealDatabase();

        Datasource datasource = new Datasource(mySQLConfig);
        BasicDataSource dataSource = datasource.getDatasource();
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
    }

    private void stopIfRealDatabase() throws Exception {
        if (!this.mySQLConfig.getUrl().contains("testing")) {
            System.out.println("Executing against real database.");
            throw new Exception("No execution against real database.");
        }
    }

    @After
    public void tearDown() throws Exception {
        connection.rollback();

        Statement statement = connection.createStatement();
        statement.executeUpdate("TRUNCATE students;");
        connection.commit();
    }
}
