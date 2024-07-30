package org.sample.students.dal.mysql;

import org.sample.students.appcontext.config.MySQLConfig;
import org.sample.students.dal.IDAOFactory;
import org.sample.students.dal.IStudentsDAO;

public class DAOFactory implements IDAOFactory {

    public StudentsDAO studentsDAO;

    public DAOFactory(MySQLConfig mySQLConfig) {
        this.studentsDAO = new StudentsDAO(mySQLConfig);
    }

    @Override
    public IStudentsDAO getStudentsDAO() {
        return this.studentsDAO;
    }
}
