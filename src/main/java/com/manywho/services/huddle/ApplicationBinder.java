package com.manywho.services.huddle;

import com.manywho.services.huddle.api.HuddleClientFactory;
import com.manywho.services.huddle.auth.AuthManager;
import com.manywho.services.huddle.documents.DocumentManager;
import com.manywho.services.huddle.files.FileManager;
import com.manywho.services.huddle.folders.FolderDatabase;
import com.manywho.services.huddle.folders.FolderRepository;
import com.manywho.services.huddle.oauth2.HuddleProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(ApplicationConfiguration.class).to(ApplicationConfiguration.class);
        bind(AuthManager.class).to(AuthManager.class);
        bind(DocumentManager.class).to(DocumentManager.class);
        bind(FileManager.class).to(FileManager.class);
        bind(FolderDatabase.class).to(FolderDatabase.class);
        bind(FolderRepository.class).to(FolderRepository.class);
        bind(HuddleClientFactory.class).to(HuddleClientFactory.class);
        bind(HuddleProvider.class).to(HuddleProvider.class);
    }
}
