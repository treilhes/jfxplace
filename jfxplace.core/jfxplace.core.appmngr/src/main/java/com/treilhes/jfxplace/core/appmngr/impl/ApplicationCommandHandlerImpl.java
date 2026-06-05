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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;
import com.treilhes.emc4j.boot.api.loader.RestartCommandEvent;
import com.treilhes.emc4j.boot.api.loader.RestartedCommandEvent;
import com.treilhes.emc4j.boot.api.loader.StopCommandEvent;
import com.treilhes.jfxplace.core.api.application.CommandEventHandler;
import com.treilhes.jfxplace.core.api.application.CommandHandler;
import com.treilhes.jfxplace.core.api.javafx.JavafxThreadHolder;
import com.treilhes.jfxplace.core.api.ui.dialog.ApplicationDialog;

@ApplicationSingleton
public class ApplicationCommandHandlerImpl implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationCommandHandlerImpl.class);

    private final ApplicationDialog applicationDialog;

    private final JavafxThreadHolder fxThreadHolder;

    private final CommandEventHandler commandEventHandler;
    /**
     * Commands waiting for the javafx thread to start
     */
    private final List<OpenCommandEvent> waitingCommands = new ArrayList<>();

    private final ApplicationImpl application;

    //@formatter:off
    public ApplicationCommandHandlerImpl(
            ApplicationImpl application,
            CommandEventHandler commandEventHandler,
            JavafxThreadHolder fxThreadHolder,
            ApplicationDialog applicationDialog) {
        //@formatter:on
        super();
        this.application = application;
        this.commandEventHandler = commandEventHandler;
        this.fxThreadHolder = fxThreadHolder;
        this.applicationDialog = applicationDialog;

        this.fxThreadHolder.whenStarted(this::executeStoredCommands);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        switch (event) {
        case OpenCommandEvent openCommandEvent -> execute(openCommandEvent);
        case StopCommandEvent stopCommandEvent -> execute(stopCommandEvent);
        case RestartCommandEvent restartCommandEvent -> execute(restartCommandEvent);
        case RestartedCommandEvent restartedCommandEvent -> execute(restartedCommandEvent);
        default -> {/* ignore other events */ }
        }

    }

    /**
     * Execute the stored commands. This method is called when the JavaFX
     * application thread is started.
     */
    private synchronized void executeStoredCommands() {
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

        application.finalizeApplication();
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

    private void execute(RestartedCommandEvent event) {
        logger.info("CMD executed " + event.toString());

        try {
            commandEventHandler.handleRestartedCommand(event);
        } catch (Exception e) {
            logger.error("Error while executing command", e);
            applicationDialog.addError("Error while executing command", e.getMessage(), e);
        }
    }
}
