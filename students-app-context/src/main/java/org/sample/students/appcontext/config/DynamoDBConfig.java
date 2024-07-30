package org.sample.students.appcontext.config;

public class DynamoDBConfig extends AbstractConfig implements Configuration {

    protected static DynamoDBConfig instance;
    private boolean useLocal;

    private String localUrl;

    private DynamoDBConfig() {
        super("api.properties", "dynamodb");
        this.useLocal = this.getProperty("useLocal").equals("True");
        this.localUrl = this.getProperty("localUrl");
    }

    public DynamoDBConfig(String localUrl) {
        super();
        this.localUrl = localUrl;
        this.useLocal = true;
    }

    public static DynamoDBConfig getInstance() {
        if (instance == null) {
            instance = new DynamoDBConfig();
        }

        return instance;
    }

    public boolean getUseLocal() {
        return useLocal;
    }

    public String getLocalUrl() {
        return localUrl;
    }
}
