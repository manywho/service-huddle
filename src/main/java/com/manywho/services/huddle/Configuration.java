package com.manywho.services.huddle;

import com.manywho.sdk.services.annotations.Property;

public class Configuration {
    @Property("Workspace ID")
    private String workspace;

    public String getWorkspace() {
        return workspace;
    }
}
