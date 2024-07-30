package org.sample.students.dal.dynamodb;

import org.sample.students.appcontext.config.DynamoDBConfig;
import org.sample.students.dal.IDAOFactory;

public class DAOFactory implements IDAOFactory {
    private StudentsDAO studentsDAO;
    public DAOFactory(DynamoDBConfig dynamoDBConfig) {
        this.studentsDAO = new StudentsDAO(dynamoDBConfig);
    }

    @Override
    public StudentsDAO getStudentsDAO() {
        return this.studentsDAO;
    }
}
