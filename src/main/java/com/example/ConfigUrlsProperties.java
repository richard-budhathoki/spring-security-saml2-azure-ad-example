package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigUrlsProperties {


    @Value("${config-urls.logout-url}")
    private String logoutUrl;


    @Value("${config-urls.single-logout-url}")
    private String singleLogoutUrl;

    @Value("${config-urls.meta-data-url}")
    private String metadataUrl;

    public String getLogoutUrl() {
        return logoutUrl;
    }
    public String getSingleLogoutUrl() {
        return singleLogoutUrl;
    }

    public String getMetadataUrl() {
        return metadataUrl;
    }


    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public void setSingleLogoutUrl(String singleLogoutUrl) {
        this.singleLogoutUrl = singleLogoutUrl;
    }

    public void setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }
}
