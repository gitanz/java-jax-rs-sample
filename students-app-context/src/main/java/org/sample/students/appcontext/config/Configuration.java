package org.sample.students.appcontext.config;

public interface Configuration {
    public void setConfiguration() throws Exception;

    public String getProperty(String propertyName);
}
