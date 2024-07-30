package org.sample.students.appcontext.config;

public class ConfigurationManager {
    private ApiConfig apiConfig;
    private MySQLConfig mySQLConfig;
    private DynamoDBConfig dynamoDBConfig;

    public ConfigurationManager() {
    }

    public ApiConfig getApiConfig() {
        return apiConfig;
    }

    public void setApiConfig() {
        this.apiConfig = ApiConfig.getInstance();
    }

    public MySQLConfig getMySQLConfig() {
        return mySQLConfig;
    }

    public void setMySQLConfig() {
        this.mySQLConfig = MySQLConfig.getInstance();
    }

    public DynamoDBConfig getDynamoDBConfig() {
        return dynamoDBConfig;
    }

    public void setDynamoDBConfig() {
        this.dynamoDBConfig = DynamoDBConfig.getInstance();
    }
}
