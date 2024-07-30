package org.sample.students.dal.mysql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import java.sql.*;

public class DatasourceTest extends BaseTest {

    @Test
    public void testCanGetConnectionFromConfig() {
        Datasource datasource = new Datasource(mySQLConfig);
        BasicDataSource dataSource = datasource.getDatasource();
        assert dataSource != null;
    }

    @Test
    public void testCanMakeConnectionUsingDatasource() {
        Datasource datasource = new Datasource(mySQLConfig);
        BasicDataSource dataSource = datasource.getDatasource();

        Connection connection = null;
        ResultSet resultSet = null;
        int number = 0;
        try{
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT 1 as number");
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            number = resultSet.getInt("number");

        }catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if(connection != null) {
                    connection.close();
                }

                if(resultSet != null) {
                    resultSet.close();
                }
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        }

        assert number == 1;
    }
}
