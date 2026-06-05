/*
 * Copyright (c) 2016, 2025, Gluon and/or its affiliates.
 * Copyright (c) 2021, 2025, Pascal Treilhes and/or its affiliates.
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation and Gluon nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.treilhes.jfxplace.core.appmngr.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.api.application.Application;
import com.treilhes.jfxplace.core.api.application.InstancesManager;
import com.treilhes.jfxplace.core.api.fs.FileSystem;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstanceUi;
import com.treilhes.jfxplace.core.api.javafx.JavaFxWindowOwnerRegistry;
import com.treilhes.jfxplace.core.api.javafx.JfxPlaceExecutor;
import com.treilhes.jfxplace.core.api.lifecycle.DisposeWithDocument;
import com.treilhes.jfxplace.core.api.lifecycle.InitWithDocument;
import com.treilhes.jfxplace.core.api.subjects.ApplicationInstanceEvents;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import javafx.stage.Stage;

/**
 * The ApplicationInstanceController class is responsible for managing the lifecycle of an application instance.
 * It handles the initialization and disposal of the application instance
 * This class is annotated with @ApplicationInstanceSingleton, indicating that only one instance of this class
 * should exist per instance context.
 *
 * The class uses the PostConstruct annotation to specify a method that should be run after the instance has been
 * constructed and dependency injection has been performed.
 *
 * It implements the com.treilhes.jfxplace.core.api.application.ApplicationInstance interface, which defines the
 * contract for an application instance in the system.
 *
 */
@ApplicationInstanceSingleton
public class ApplicationInstanceImpl implements ApplicationInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInstanceImpl.class);

    private final Application application;

    private final EmContext context;
    private final JfxPlaceExecutor executor;
    private final JavaFxWindowOwnerRegistry windowOwnerRegistry;
    private final Provider<FileSystem> fileSystem;

    private final ApplicationInstanceEvents applicationInstanceEvents;
    private final Provider<Optional<List<InitWithDocument>>> initializations;
    private final Provider<Optional<List<DisposeWithDocument>>> finalizations;

    private final InstancesManager main;
    private final Provider<ApplicationInstanceUi> ui;

    // @formatter:off
    public ApplicationInstanceImpl(
            Application application,
            EmContext context,
            ApplicationInstanceEvents documentManager,
            InstancesManager main,
            Provider<FileSystem> fileSystem,
            JavaFxWindowOwnerRegistry windowOwnerRegistry,
            Provider<ApplicationInstanceUi> ui,
            Provider<Optional<List<InitWithDocument>>> initializations,
            Provider<Optional<List<DisposeWithDocument>>> finalizations
    ) {
     // @formatter:on
        super();
        this.application = application;
        this.context = context;
        this.fileSystem = fileSystem;
        this.windowOwnerRegistry = windowOwnerRegistry;
        this.main = main;
        this.applicationInstanceEvents = documentManager;
        this.ui = ui;
        this.initializations = initializations;
        this.finalizations = finalizations;

        this.executor = new JfxPlaceExecutor(context);

    }

    @PostConstruct
    public void init()  {
        // now that the instance is fully initialized, we can start the UI and the file system watcher
        try {
            var instanceUi = ui.get();
            // Initializes InitWithDocument beans if any
            initializations.get().ifPresent(l -> l.forEach(a -> a.initWithDocument()));
        } catch (Exception e) {
            LOGGER.error("Error during application instance UI initialization", e);
        }

        // Starts the file system watcher
        fileSystem.get().startWatcher();
    }

    @PreDestroy
    public void dispose() {
        // Stops the file system watcher
        fileSystem.get().stopWatcher();
        // Disposes DisposeWithDocument beans if any
        finalizations.get().ifPresent(f -> f.forEach(a -> a.disposeWithDocument()));
    }

    @Override
    public EmContext getContext() {
        return context;
    }

    @Override
    public ApplicationInstanceEvents getEvents() {
        return applicationInstanceEvents;
    }

    @Override
    public JfxPlaceExecutor getExecutor() {
        return executor;
    }

    @Override
    public ApplicationInstanceUi getUi() {
        return ui.get();
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public Stage newStage() {
        var stage = new Stage();
        windowOwnerRegistry.register(stage, this);
        return stage;
    }
}

