package com.manywho.services.huddle.documents;

import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.sdk.enums.InvokeType;
import com.manywho.sdk.services.PropertyCollectionParser;
import com.manywho.services.huddle.api.HuddleClientFactory;

import javax.inject.Inject;

public class DocumentManager {
    private final PropertyCollectionParser propertyCollectionParser;
    private final HuddleClientFactory huddleClientFactory;

    @Inject
    public DocumentManager(PropertyCollectionParser propertyCollectionParser, HuddleClientFactory huddleClientFactory) {
        this.propertyCollectionParser = propertyCollectionParser;
        this.huddleClientFactory = huddleClientFactory;
    }

    public ServiceResponse moveFile(AuthenticatedWho user, ServiceRequest serviceRequest) throws Exception {
        DocumentMove documentMove = propertyCollectionParser.parse(serviceRequest.getInputs(), DocumentMove.class);

        huddleClientFactory.create(user.getToken())
                .moveDocument(documentMove.getFile().getId(), documentMove.getFolderId());

        return new ServiceResponse(InvokeType.Forward, serviceRequest.getToken());
    }
}
