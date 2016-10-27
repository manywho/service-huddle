package com.manywho.services.huddle.api.entities;

import java.util.List;

public class Folder {
    private String title;
    private String description;
    private List<Document> documents;
    private List<Link> links;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Link> getLinks() {
        return links;
    }
}
