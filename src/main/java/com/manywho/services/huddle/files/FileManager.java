package com.manywho.services.huddle.files;

import com.manywho.sdk.entities.run.elements.type.FileDataRequest;
import com.manywho.sdk.entities.run.elements.type.MObject;
import com.manywho.sdk.entities.run.elements.type.ObjectCollection;
import com.manywho.sdk.entities.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.entities.run.elements.type.Property;
import com.manywho.sdk.entities.run.elements.type.PropertyCollection;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.sdk.services.types.TypeBuilder;
import com.manywho.services.huddle.api.HuddleClient;
import com.manywho.services.huddle.api.HuddleClientFactory;
import com.manywho.services.huddle.api.entities.Document;
import com.manywho.services.huddle.api.entities.Folder;
import com.manywho.services.huddle.api.entities.Link;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.inject.Inject;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileManager {
    private final HuddleClientFactory huddleClientFactory;
    private final TypeBuilder typeBuilder;

    @Inject
    public FileManager(HuddleClientFactory huddleClientFactory, TypeBuilder typeBuilder) {
        this.huddleClientFactory = huddleClientFactory;
        this.typeBuilder = typeBuilder;
    }

    public ObjectDataResponse loadFiles(AuthenticatedWho user, FileDataRequest fileDataRequest) {
        Folder folder = huddleClientFactory.create(user.getToken())
                .loadFolder(fileDataRequest.getResourcePath());

        ObjectCollection files = folder.getDocuments().stream()
                .map(this::createFileObject)
                .collect(Collectors.toCollection(ObjectCollection::new));

        return new ObjectDataResponse(files);
    }

    public ObjectDataResponse uploadFile(AuthenticatedWho user, FileDataRequest fileDataRequest, FormDataMultiPart file) {
        HuddleClient huddleClient = huddleClientFactory.create(user.getToken());

        BodyPart filePart = getFilePart(file);

        Document document = huddleClient.createDocument(
                fileDataRequest.getResourcePath(),
                filePart.getMediaType().toString(),
                filePart.getEntityAs(byte[].class).length,
                filePart.getContentDisposition().getFileName(),
                filePart.getContentDisposition().getFileName(),
                FilenameUtils.getExtension(filePart.getContentDisposition().getFileName())
        );

        Link uploadLink = document.getLinks().stream()
                .filter(link -> link.getRel().equals("upload"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to find an 'upload' link for the created document"));

        huddleClient.uploadFile(uploadLink.getHref(), filePart.getEntityAs(InputStream.class));

        return new ObjectDataResponse();
    }

    private BodyPart getFilePart(FormDataMultiPart formDataMultiPart) {
        // If the filename is blank or doesn't exist, assume it's the FileDataRequest and skip it
        Optional<BodyPart> filePart = formDataMultiPart.getBodyParts().stream()
                .filter(bodyPart -> StringUtils.isNotEmpty(bodyPart.getContentDisposition().getFileName()))
                .findFirst();

        if (filePart.isPresent()) {
            return filePart.get();
        }

        throw new RuntimeException("A file could not be found in the received request");
    }

    public MObject createFileObject(Document document) {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = LocalDateTime.now();

        // Look for the hypermedia link that contains the document's "self" URL so we can extract the ID
        Link selfLink = document.getLinks().stream()
                .filter(link -> link.getRel().equals("self"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to find a 'self' link for the current Huddle document"));

        // Look for the hypermedia link that contains the document's "content" URL so we can use it as the download URL
        Link contentLink = document.getLinks().stream()
                .filter(link -> link.getRel().equals("content"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to find a 'content' link for the current Huddle document"));

        String id = selfLink.getHref().replace("https://api.huddle.net/files/documents/", "");

        PropertyCollection properties = new PropertyCollection();
        properties.add(new Property("Kind", document.getExtension()));
        properties.add(new Property("ID", id));
        properties.add(new Property("Mime Type", document.getMimeType()));
        properties.add(new Property("Name", document.getTitle()));
        properties.add(new Property("Description", document.getDescription()));
        properties.add(new Property("Date Created", created));
        properties.add(new Property("Date Modified", updated));
        properties.add(new Property("Download Uri", contentLink.getHref()));
        properties.add(new Property("Embed Uri"));
        properties.add(new Property("Icon Uri"));

        MObject object = new MObject();
        object.setDeveloperName("$File");
        object.setExternalId(id);
        object.setProperties(properties);

        return object;
    }
}
