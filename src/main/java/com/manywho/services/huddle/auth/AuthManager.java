package com.manywho.services.huddle.auth;

import com.manywho.sdk.entities.security.AuthenticatedWhoResult;
import com.manywho.sdk.entities.security.AuthenticationCredentials;
import com.manywho.sdk.enums.AuthenticationStatus;
import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import com.manywho.services.huddle.api.HuddleClientFactory;
import com.manywho.services.huddle.api.entities.Link;
import com.manywho.services.huddle.api.entities.User;
import com.manywho.services.huddle.oauth2.HuddleService;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import javax.inject.Inject;

public class AuthManager {
    private final HuddleClientFactory huddleClientFactory;

    @Inject
    public AuthManager(HuddleClientFactory huddleClientFactory) {
        this.huddleClientFactory = huddleClientFactory;
    }

    public AuthenticatedWhoResult authentication(AbstractOauth2Provider oauth2Provider, OAuthService oauthService, AuthenticationCredentials credentials) throws Exception {
        HuddleService huddleService = new HuddleService(oauth2Provider, oauthService);

        // Retrieve the access token from the Huddle API using the given authorization code
        Token accessToken = huddleService.getAccessToken(null, new Verifier(credentials.getCode()));

        // Get the currently logged-in user from Huddle
        User user = huddleClientFactory.create(accessToken.getToken())
                .getCurrentUser();

        // Look for the contact object that contains the user's email address
        User.Profile.Contact emailContact = user.getProfile().getContacts().stream()
                .filter(c -> c.getRel().equals("mail"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to find an email address for the current Huddle user"));

        // Look for the hypermedia link that contains the user's "self" URL so we can extract their ID
        Link selfLink = user.getLinks().stream()
                .filter(link -> link.getRel().equals("self"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to find a 'self' link for the current Huddle user"));

        // Create the user result for ManyWho, with a bunch of faked fields as Huddle doesn't provide them
        AuthenticatedWhoResult authenticatedWhoResult = new AuthenticatedWhoResult();
        authenticatedWhoResult.setDirectoryId("huddle");
        authenticatedWhoResult.setDirectoryName("Huddle");
        authenticatedWhoResult.setEmail(emailContact.getValue());
        authenticatedWhoResult.setFirstName(user.getProfile().getPersonal().getFirstName());
        authenticatedWhoResult.setIdentityProvider("Huddle");
        authenticatedWhoResult.setLastName(user.getProfile().getPersonal().getSurname());
        authenticatedWhoResult.setStatus(AuthenticationStatus.Authenticated);
        authenticatedWhoResult.setTenantName("Huddle");
        authenticatedWhoResult.setToken(accessToken.getToken());
        authenticatedWhoResult.setUserId(selfLink.getHref().replace("https://api.huddle.net/users/", ""));
        authenticatedWhoResult.setUsername(emailContact.getValue());

        return authenticatedWhoResult;
    }
}
