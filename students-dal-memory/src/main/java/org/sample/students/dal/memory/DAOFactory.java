package org.sample.students.dal.memory;

import org.sample.students.dal.IDAOFactory;
import org.sample.students.dal.IStudentsDAO;

public class DAOFactory implements IDAOFactory {
    public StudentsDAO studentsDAO;

    public DAOFactory() {
        this.studentsDAO = new StudentsDAO();
    }

    @Override
    public IStudentsDAO getStudentsDAO() {
        return this.studentsDAO;
    }
}
