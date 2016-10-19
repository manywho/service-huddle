package com.manywho.services.huddle.api;

public class HuddleClientFactory {
    public HuddleClient create(String accessToken) {
        return new HuddleClient(accessToken);
    }
}
