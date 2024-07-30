package org.sample.students.appcontext.config;

public class MySQLConfig extends AbstractConfig implements Configuration {
    protected static MySQLConfig instance;
    protected String propertyPrefix = "mysql";
    protected String configurationPath = "api.properties";
    private String url;
    private String username;
    private String password;

    private MySQLConfig() {
        super("api.properties", "mysql");
        this.url = this.getProperty("url");
        this.username = this.getProperty("username");
        this.password = this.getProperty("password");
    }

    public MySQLConfig(String url, String username, String password) {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static MySQLConfig getInstance() {
        if (instance == null) {
            instance = new MySQLConfig();
            instance.url = instance.getProperty("url");
            instance.username = instance.getProperty("username");
            instance.password = instance.getProperty("password");
        }

        return instance;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
