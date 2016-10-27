package com.manywho.services.huddle.folders;

import com.manywho.services.huddle.api.HuddleClient;
import com.manywho.services.huddle.api.entities.Folder;
import com.manywho.services.huddle.api.entities.Link;

import java.util.Optional;

public class FolderRepository {
    public FolderType create(HuddleClient client, FolderType folderType) {
        Folder folder = client
                .createFolder(folderType.getParent().getId(), folderType.getTitle(), folderType.getDescription());

        return createType(folder);
    }

    public FolderType find(HuddleClient client, String workspace, String id) {
        Folder folder = client.loadFolder(workspace, id);

        return createType(folder);
    }

    /**
     * TODO: Move this - it shouldn't be in the repository
     */
    private FolderType createType(Folder folder) {
        Link selfLink = folder.getLinks().stream()
                .filter(link -> link.getRel().equals("self"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to find a 'self' link for the current Huddle folder"));

        Optional<Link> parentLink = folder.getLinks().stream()
                .filter(link -> link.getRel().equals("parent"))
                .findFirst();

        FolderType folderType = new FolderType();
        folderType.setDescription(folder.getDescription());
        folderType.setId(selfLink.getHref().replace("https://api.huddle.net/files/folders/", ""));
        folderType.setTitle(folder.getTitle());

        if (parentLink.isPresent()) {
            FolderType parentFolderType = new FolderType();
            parentFolderType.setId(parentLink.get().getHref().replace("https://api.huddle.net/files/folders/", ""));

            folderType.setParent(parentFolderType);
        }

        return folderType;
    }
}
