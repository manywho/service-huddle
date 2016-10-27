package com.manywho.services.huddle.api.entities;

public class Link {
    private String rel;
    private String href;
    private String type;

    public Link() {
    }

    public Link(String rel, String href) {
        this.rel = rel;
        this.href = href;
    }

    public Link(String rel, String href, String type) {
        this.rel = rel;
        this.href = href;
        this.type = type;
    }

    public String getRel() {
        return rel;
    }

    public String getHref() {
        return href;
    }

    public String getType() {
        return type;
    }
}
