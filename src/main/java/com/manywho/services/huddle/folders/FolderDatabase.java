package com.manywho.services.huddle.folders;

import com.manywho.sdk.entities.run.elements.type.ObjectCollection;
import com.manywho.sdk.entities.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.sdk.services.types.TypeBuilder;
import com.manywho.sdk.services.types.TypeParser;
import com.manywho.services.huddle.Configuration;
import com.manywho.services.huddle.api.HuddleClient;
import com.manywho.services.huddle.api.HuddleClientFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class FolderDatabase {
    private final HuddleClientFactory huddleClientFactory;
    private final TypeBuilder typeBuilder;
    private final TypeParser typeParser;
    private final FolderRepository folderRepository;

    @Inject
    public FolderDatabase(HuddleClientFactory huddleClientFactory, TypeBuilder typeBuilder, TypeParser typeParser, FolderRepository folderRepository) {
        this.huddleClientFactory = huddleClientFactory;
        this.typeBuilder = typeBuilder;
        this.typeParser = typeParser;
        this.folderRepository = folderRepository;
    }

    public ObjectCollection load(AuthenticatedWho user, Configuration configuration, ObjectDataRequest request) throws Exception {
        HuddleClient client = huddleClientFactory.create(user.getToken());

        // If we have an ID then we should load that single folder
        if (request.getListFilter().getId() != null) {
            FolderType folderType = folderRepository.find(client, configuration.getWorkspace(), request.getListFilter().getId());

            return typeBuilder.buildObject(request, folderType, FolderType.class);
        }

        return new ObjectCollection();
    }

    public ObjectCollection save(AuthenticatedWho user, Configuration configuration, ObjectDataRequest request) throws Exception {
        HuddleClient client = huddleClientFactory.create(user.getToken());

        // If we don't have an ID then we want to create a new folder
        if (request.getListFilter().getId() == null) {

            // Parse the incoming object data into a list of Folder types
            List<FolderType> folders = typeParser.parseList(request.getObjectData(), FolderType.class).stream()
                    .map(folderType -> folderRepository.create(client, folderType))
                    .collect(Collectors.toList());

            // Create the Types from the created folders
            return typeBuilder.buildList(request, folders, FolderType.class);
        }

        return new ObjectCollection();
    }
}
