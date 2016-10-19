package com.manywho.services.huddle.oauth2;

import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import com.manywho.services.huddle.ApplicationConfiguration;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;

import javax.inject.Inject;

public class HuddleProvider extends AbstractOauth2Provider {
    private final ApplicationConfiguration configuration;

    @Inject
    public HuddleProvider(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://login.huddle.net/token";
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format("https://login.huddle.net/request?response_type=code&client_id=%s", config.getApiKey());
    }

    @Override
    public String getName() {
        return "Huddle";
    }

    @Override
    public String getClientId() {
        return configuration.getOauth2ClientId();
    }

    @Override
    public String getClientSecret() {
        return "none";
    }

    @Override
    public String getRedirectUri() {
        return "https://flow.manywho.com/api/run/1/oauth2";
    }
}
