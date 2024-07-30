package org.sample.students.appcontext.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

abstract class AbstractConfig {
    protected String propertyPrefix;
    protected String configurationPath;
    protected Configuration configuration;


    public AbstractConfig() {
    }

    protected AbstractConfig(String configurationPath, String propertyPrefix) {
        this.configurationPath = configurationPath;
        this.propertyPrefix = propertyPrefix;
        this.setConfiguration();
    }

    public void setConfiguration() {
        Configurations configs = new Configurations();
        try {
            Configuration config = configs.properties(new File(configurationPath));
            this.configuration = config.subset(propertyPrefix);
        } catch (ConfigurationException cex) {
        }
    }

    public String getProperty(String propertyName) {

        if (this.configuration == null) {
            return null;
        }

        Object value = this.configuration.getProperty(propertyName);

        if (value == null) {
            return null;
        }

        return value.toString();
    }
}
