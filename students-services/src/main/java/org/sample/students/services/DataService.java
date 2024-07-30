package org.sample.students.services;

import org.sample.students.appcontext.ApplicationContext;
import org.sample.students.dal.IDAOFactory;
import org.sample.students.dal.mysql.DAOFactory;

public class DataService {
    private static DataService instance;
    private final IDAOFactory daoFactory;
    private DataService() {
        String dataSource = ApplicationContext
                .getConfigurationsManager()
                .getApiConfig()
                .getDataSource();

        if (dataSource.equals("mysql")) {
            this.daoFactory = new DAOFactory(ApplicationContext.getConfigurationsManager().getMySQLConfig());
        }
        else if (dataSource.equals("dynamodb")) {
            this.daoFactory = new DAOFactory(ApplicationContext.getConfigurationsManager().getDynamoDBConfig());
        }
        else {
            this.daoFactory = new org.sample.students.dal.memory.DAOFactory();
        }

    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }

        return instance;
    }

    public static IDAOFactory getDAOFactory() {
        return getInstance().daoFactory;
    }
}
