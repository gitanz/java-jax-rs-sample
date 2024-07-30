package org.sample.students.appcontext.config;

public class ApiConfig extends AbstractConfig implements Configuration {
    protected static ApiConfig instance;

    private ApiConfig() {
        super("api.properties", "api");
    }

    public static ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }

        return instance;
    }

    public String getDataSource() {
        return configuration.getString("datasource");
    }
}
