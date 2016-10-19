package com.manywho.services.huddle;

import com.manywho.sdk.services.config.ServiceConfiguration;

import javax.inject.Inject;

public class ApplicationConfiguration {
    private final ServiceConfiguration configuration;

    @Inject
    public ApplicationConfiguration(ServiceConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getOauth2ClientId() {
        return configuration.get("oauth2.clientId");
    }
}
