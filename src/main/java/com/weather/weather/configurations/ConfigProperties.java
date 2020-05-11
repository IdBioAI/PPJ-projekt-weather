package com.weather.weather.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "weather")
public class ConfigProperties {

    private int updateTime;
    private boolean readOnly;
    private boolean importOnStartUp;
    private String Import;

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isImportOnStartUp() {
        return importOnStartUp;
    }

    public void setImportOnStartUp(boolean importOnStartUp) {
        this.importOnStartUp = importOnStartUp;
    }

    public String getImport() {
        return Import;
    }

    public void setImport(String anImport) {
        Import = anImport;
    }
}
