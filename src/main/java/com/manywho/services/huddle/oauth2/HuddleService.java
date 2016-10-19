package com.manywho.services.huddle.oauth2;

import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class HuddleService implements OAuthService {
    private final AbstractOauth2Provider provider;
    private final OAuthService oAuthService;

    public HuddleService(AbstractOauth2Provider provider, OAuthService oAuthService) {
        this.provider = provider;
        this.oAuthService = oAuthService;
    }

    @Override
    public Token getRequestToken() {
        return oAuthService.getRequestToken();
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(provider.getAccessTokenVerb(), provider.getAccessTokenEndpoint());

        request.addHeader("Accept", "application/json");

        request.addBodyParameter(OAuthConstants.CLIENT_ID, provider.getClientId());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, provider.getRedirectUri());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, provider.getClientSecret());
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());

        request.addBodyParameter("grant_type", "authorization_code");

        Response response = request.send();

        return provider.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public void signRequest(Token accessToken, OAuthRequest request) {
        oAuthService.signRequest(accessToken, request);
    }

    @Override
    public String getVersion() {
        return oAuthService.getVersion();
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return oAuthService.getAuthorizationUrl(requestToken);
    }
}
