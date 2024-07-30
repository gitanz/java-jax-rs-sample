package org.sample.students.dal.mysql;

import org.junit.Test;


public class DAOFactoryTest extends BaseTest {

    @Test
    public void testMySQLDAOFactoryInitializesStudentsDAO() {
        DAOFactory mysqlDAOFactory = new DAOFactory(mySQLConfig);
        assert mysqlDAOFactory.studentsDAO != null;
    }
}
