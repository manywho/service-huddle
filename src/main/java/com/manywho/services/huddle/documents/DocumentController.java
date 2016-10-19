package com.manywho.services.huddle.documents;

import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.services.controllers.AbstractController;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/documents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DocumentController extends AbstractController {
    private final DocumentManager manager;

    @Inject
    public DocumentController(DocumentManager manager) {
        this.manager = manager;
    }

    @Path("/move")
    @POST
    public ServiceResponse moveFile(ServiceRequest serviceRequest) throws Exception {
        return manager.moveFile(getAuthenticatedWho(), serviceRequest);
    }
}
