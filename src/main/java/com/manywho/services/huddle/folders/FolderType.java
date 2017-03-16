package com.manywho.services.huddle.folders;

import com.manywho.sdk.enums.ContentType;
import com.manywho.sdk.services.annotations.Id;
import com.manywho.sdk.services.annotations.TypeElement;
import com.manywho.sdk.services.annotations.TypeProperty;

@TypeElement(name = FolderType.NAME)
public class FolderType {
    public final static String NAME = "Folder";

    @Id
    @TypeProperty(name = "ID", contentType = ContentType.String)
    private String id;

    @TypeProperty(name = "Title", contentType = ContentType.String)
    private String title;

    @TypeProperty(name = "Description", contentType = ContentType.String)
    private String description;

    @TypeProperty(name = "Parent Folder", contentType = ContentType.Object)
    private FolderType parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FolderType getParent() {
        return parent;
    }

    public void setParent(FolderType parent) {
        this.parent = parent;
    }
}
