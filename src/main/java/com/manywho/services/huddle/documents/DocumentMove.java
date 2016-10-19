package com.manywho.services.huddle.documents;

import com.manywho.sdk.services.annotations.Property;
import com.manywho.sdk.services.types.system.$File;

public class DocumentMove {
    @Property(value = "File", isObject = true)
    private $File file;

    @Property(value = "Folder ID")
    private String folderId;

    public $File getFile() {
        return file;
    }

    public String getFolderId() {
        return folderId;
    }
}
