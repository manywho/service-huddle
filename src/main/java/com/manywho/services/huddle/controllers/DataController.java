package com.manywho.services.huddle.controllers;

import com.manywho.sdk.entities.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.entities.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.services.controllers.AbstractDataController;
import com.manywho.services.huddle.Configuration;
import com.manywho.services.huddle.folders.FolderType;
import com.manywho.services.huddle.folders.FolderDatabase;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataController extends AbstractDataController {
    private final FolderDatabase folderDatabase;

    @Inject
    public DataController(FolderDatabase folderDatabase) {
        this.folderDatabase = folderDatabase;
    }

    @Override
    public ObjectDataResponse delete(ObjectDataRequest objectDataRequest) throws Exception {
        throw new RuntimeException("Deleting isn't yet supported in this service");
    }

    @Override
    public ObjectDataResponse load(ObjectDataRequest request) throws Exception {
        Configuration configuration = this.parseConfigurationValues(request, Configuration.class);

        String typeName = request.getObjectDataType().getDeveloperName();

        switch (typeName) {
            case FolderType.NAME:
                return new ObjectDataResponse(folderDatabase.load(getAuthenticatedWho(), configuration, request));
        }

        throw new RuntimeException("Loading the Type " + typeName + " isn't supported in this service");
    }

    @Override
    public ObjectDataResponse save(ObjectDataRequest request) throws Exception {
        Configuration configuration = this.parseConfigurationValues(request, Configuration.class);

        String typeName = request.getObjectDataType().getDeveloperName();

        switch (typeName) {
            case FolderType.NAME:
                return new ObjectDataResponse(folderDatabase.save(getAuthenticatedWho(), configuration, request));
        }

        throw new RuntimeException("Saving the Type " + typeName + " isn't supported in this service");
    }
}
