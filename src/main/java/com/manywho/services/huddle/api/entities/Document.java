package com.manywho.services.huddle.api.entities;

import java.util.List;

public class Document {
    private List<Link> links;
    private String title;
    private String description;
    private String extension;
    private String mimeType;
    private String updated;
    private String created;

    public List<Link> getLinks() {
        return links;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getUpdated() {
        return updated;
    }

    public String getCreated() {
        return created;
    }
}
