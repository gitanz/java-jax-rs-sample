package org.sample.students.appcontext;

import org.sample.students.appcontext.config.ConfigurationManager;

public class ApplicationContext {

    private static ApplicationContext instance;

    private ConfigurationManager configurationManager;

    private ApplicationContext() {
    }

    private static ApplicationContext getApplicationContext() {
        if (instance == null) {
            instance = new ApplicationContext();
        }

        return instance;
    }

    private void bootstrap() {
        this.registerConfigurations();
    }

    private void registerConfigurations() {
        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.setApiConfig();
        configurationManager.setDynamoDBConfig();
        configurationManager.setMySQLConfig();

        this.configurationManager = configurationManager;
    }

    public static void buildApplicationContext() {
        ApplicationContext applicationContext = getApplicationContext();
        applicationContext.bootstrap();
    }

    public static ConfigurationManager getConfigurationsManager() {
        return getApplicationContext().configurationManager;
    }
}
