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
package com.gluonhq.jfxapps.core.appmngr.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

import com.gluonhq.jfxapps.core.api.application.ApplicationActionFactory;
import com.gluonhq.jfxapps.core.api.application.ApplicationClassloader;
import com.gluonhq.jfxapps.core.api.application.CommandEventHandler;
import com.gluonhq.jfxapps.core.api.application.CommandHandler;
import com.gluonhq.jfxapps.core.api.javafx.JavafxThreadClassloaderDispatcher;
import com.gluonhq.jfxapps.core.api.javafx.JavafxThreadHolder;
import com.gluonhq.jfxapps.core.api.lifecycle.DisposeWithApplication;
import com.gluonhq.jfxapps.core.api.lifecycle.InitWithApplication;
import com.gluonhq.jfxapps.core.api.subjects.ApplicationEvents;
import com.gluonhq.jfxapps.core.api.ui.dialog.ApplicationDialog;
import com.treilhes.emc4j.boot.api.context.Application;
import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;
import com.treilhes.emc4j.boot.api.loader.RestartCommandEvent;
import com.treilhes.emc4j.boot.api.loader.StopCommandEvent;

import jakarta.inject.Provider;

@ApplicationSingleton
public class CommandHandlerImpl implements CommandHandler, Application {

    private static final Logger logger = LoggerFactory.getLogger(CommandHandlerImpl.class);

    private final ApplicationClassloader applicationClassloader;

    private final ApplicationEvents applicationEvents;

    private final ApplicationDialog applicationDialog;

    private final ApplicationActionFactory applicationActionFactory;


    private final CommandEventHandler commandEventHandler;
    /**
     * Commands waiting for the javafx thread to start
     */
    private final List<OpenCommandEvent> waitingCommands = new ArrayList<>();

    private final JavafxThreadHolder fxThreadHolder;
    private final JavafxThreadClassloaderDispatcher dispatcher;


    private final EmContext context;

    private final Provider<Optional<List<InitWithApplication>>> initializations;
    private final Provider<Optional<List<DisposeWithApplication>>> finalizations;




    //@formatter:off
    public CommandHandlerImpl(
            ApplicationActionFactory applicationActionFactory,
            EmContext context,
            CommandEventHandler commandEventHandler,
            JavafxThreadHolder fxThreadHolder,
            JavafxThreadClassloaderDispatcher dispatcher,
            ApplicationClassloader applicationClassloader,
            ApplicationEvents applicationEvents,
            ApplicationDialog applicationDialog,
            Provider<Optional<List<InitWithApplication>>> initializations,
            Provider<Optional<List<DisposeWithApplication>>> finalizations) {
        //@formatter:on
        super();
        this.applicationActionFactory = applicationActionFactory;
        this.context = context;
        this.commandEventHandler = commandEventHandler;
        this.fxThreadHolder = fxThreadHolder;
        this.applicationClassloader = applicationClassloader;
        this.applicationEvents = applicationEvents;
        this.applicationDialog = applicationDialog;
        this.initializations = initializations;
        this.finalizations = finalizations;
        this.dispatcher = dispatcher;

        this.fxThreadHolder.whenStarted(this::executeStoredCommands);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        context.getApplicationExecutor().setCurrentScope(this);
        switch (event) {
        case OpenCommandEvent openCommandEvent -> execute(openCommandEvent);
        case StopCommandEvent stopCommandEvent -> execute(stopCommandEvent);
        case RestartCommandEvent restartCommandEvent -> execute(restartCommandEvent);
        default -> logger.warn("Received an unsupported event: " + event.getClass().getName());
        }

    }

    private void initApplication() {
        applicationEvents.newWindow().subscribe((window) -> dispatcher.register(window, applicationClassloader));
        applicationEvents.classloader().set(applicationClassloader);
        initializations.get().ifPresent(l -> l.forEach(a -> a.init()));
    }

    public void finalizeApplication() {
        finalizations.get().ifPresent(f -> f.forEach(a -> a.dispose()));
    }

    /**
     * Execute the stored commands. This method is called when the JavaFX
     * application thread is started.
     */
    private synchronized void executeStoredCommands() {
        initApplication();

        while (!waitingCommands.isEmpty()) {
            OpenCommandEvent currentArgs = waitingCommands.remove(0);
            execute(currentArgs);
        }
    }

    private void execute(OpenCommandEvent command) {
        logger.info("CMD received " + command.toString());

        if (!fxThreadHolder.hasStarted()) {
            waitingCommands.add(command);
        } else {
            executeStoredCommands();
            logger.info("CMD executed " + command.toString());
            context.getApplicationExecutor().setCurrentScope(this);
            try {
                commandEventHandler.handleOpenCommand(command);
            } catch (Exception e) {
                logger.error("Error while executing command", e);
                applicationDialog.addError("Error while executing command", e.getMessage(), e);
            }
        }
    }

    private void execute(StopCommandEvent event) {
        logger.info("CMD executed " + event.toString());

        try {
            commandEventHandler.handleStopCommand(event);
        } catch (Exception e) {
            logger.error("Error while executing command", e);
            applicationDialog.addError("Error while executing command", e.getMessage(), e);
        }

        // force close the window if it exists
        applicationActionFactory.closeAllInstances().perform();

        // ensure the application is finalized even if the command handler throws an exception
        finalizeApplication();
    }

    private void execute(RestartCommandEvent event) {
        logger.info("CMD executed " + event.toString());

        try {
            commandEventHandler.handleRestartCommand(event);
        } catch (Exception e) {
            logger.error("Error while executing command", e);
            applicationDialog.addError("Error while executing command", e.getMessage(), e);
        }
    }
}
