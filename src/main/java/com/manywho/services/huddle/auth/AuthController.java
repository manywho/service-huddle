package com.manywho.services.huddle.auth;

import com.manywho.sdk.entities.security.AuthenticatedWhoResult;
import com.manywho.sdk.entities.security.AuthenticationCredentials;
import com.manywho.sdk.services.controllers.AbstractOauth2Controller;
import com.manywho.services.huddle.oauth2.HuddleProvider;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
@Consumes("application/json")
@Produces("application/json")
public class AuthController extends AbstractOauth2Controller {
    private final AuthManager manager;

    @Inject
    public AuthController(HuddleProvider provider, AuthManager manager) {
        super(provider);
        this.manager = manager;
    }

    @Override
    public AuthenticatedWhoResult authentication(AuthenticationCredentials credentials) throws Exception {
        return manager.authentication(getOauth2Provider(), getOauthService(), credentials);
    }
}