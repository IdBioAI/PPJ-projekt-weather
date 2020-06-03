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
    private boolean update;
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isImportOnStartUp() {
        return importOnStartUp;
    }

    public String getImport() {
        return Import;
    }

    public void setImport(String anImport) {
        Import = anImport;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setImportOnStartUp(boolean importOnStartUp) {
        this.importOnStartUp = importOnStartUp;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
