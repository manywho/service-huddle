package com.manywho.services.huddle.files;

import com.manywho.sdk.entities.run.elements.type.FileDataRequest;
import com.manywho.sdk.entities.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.services.controllers.AbstractController;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/file")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FileController extends AbstractController {
    private final FileManager manager;

    @Inject
    public FileController(FileManager manager) {
        this.manager = manager;
    }

    @Path("/")
    @POST
    public ObjectDataResponse loadFiles(FileDataRequest fileDataRequest) throws Exception {
        return manager.loadFiles(getAuthenticatedWho(), fileDataRequest);
    }

    @POST
    @Path("/content")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_OCTET_STREAM})
    public ObjectDataResponse uploadFile(@FormDataParam("FileDataRequest") FileDataRequest fileDataRequest, FormDataMultiPart file) throws Exception {
        return manager.uploadFile(getAuthenticatedWho(), fileDataRequest, file);
    }
}