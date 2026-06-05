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
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.spring.core.patch.PatchLink;
import com.treilhes.jfxplace.core.api.application.Application;
import com.treilhes.jfxplace.core.api.application.ApplicationActionFactory;
import com.treilhes.jfxplace.core.api.application.ApplicationClassloader;
import com.treilhes.jfxplace.core.api.i18n.I18N;
import com.treilhes.jfxplace.core.api.javafx.JavaFxWindowOwnerRegistry;
import com.treilhes.jfxplace.core.api.javafx.JfxPlaceExecutor;
import com.treilhes.jfxplace.core.api.lifecycle.DisposeWithApplication;
import com.treilhes.jfxplace.core.api.lifecycle.InitWithApplication;
import com.treilhes.jfxplace.core.api.subjects.ApplicationEvents;
import com.treilhes.jfxplace.core.api.ui.controller.misc.IconSetting;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Provider;
import javafx.stage.Stage;

@ApplicationSingleton
public class ApplicationImpl implements Application {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationImpl.class);

    private final ApplicationClassloader applicationClassloader;

    private final ApplicationEvents applicationEvents;

    private final ApplicationActionFactory applicationActionFactory;

    private final JavaFxWindowOwnerRegistry windowsRegistry;


    private final EmContext context;
    private final I18N i18n;
    private final JfxPlaceExecutor executor;
    private final IconSetting iconSetting;
    private final Provider<Optional<List<InitWithApplication>>> initializations;
    private final Provider<Optional<List<DisposeWithApplication>>> finalizations;

    //@formatter:off
    public ApplicationImpl(
            I18N i18n,
            IconSetting iconSetting,
            ApplicationActionFactory applicationActionFactory,
            EmContext context,
            JavaFxWindowOwnerRegistry windowsRegistry,
            ApplicationClassloader applicationClassloader,
            ApplicationEvents applicationEvents,
            Provider<Optional<List<InitWithApplication>>> initializations,
            Provider<Optional<List<DisposeWithApplication>>> finalizations) {
        //@formatter:on
        super();
        this.i18n = i18n;
        this.iconSetting = iconSetting;
        this.applicationActionFactory = applicationActionFactory;
        this.context = context;
        this.applicationClassloader = applicationClassloader;
        this.applicationEvents = applicationEvents;
        this.initializations = initializations;
        this.finalizations = finalizations;
        this.windowsRegistry = windowsRegistry;

        this.executor = new JfxPlaceExecutor(context);

        logger.debug("CommandHandlerImpl initialized");
    }

    @PostConstruct
    protected void initApplication() {
        applicationEvents.classloader().set(applicationClassloader);
        initializations.get().ifPresent(l -> l.forEach(a -> a.init()));
    }


    protected void finalizeApplication() {
        // force close the window if it exists
        applicationActionFactory.closeAllInstances().perform();
        finalizations.get().ifPresent(f -> f.forEach(a -> a.dispose()));
        applicationEvents.terminate();
        windowsRegistry.terminate(this);
        PatchLink.clearFactoriesCache(applicationClassloader);
    }

    @Override
    public EmContext getContext() {
        return context;
    }

    @Override
    public ApplicationEvents getEvents() {
        return applicationEvents;
    }

    @Override
    public JfxPlaceExecutor getExecutor() {
        return executor;
    }

    @Override
    public I18N getI18n() {
        return i18n;
    }

    @Override
    public IconSetting getIconSettings() {
        return iconSetting;
    }

    @Override
    public Stage newStage() {
        var stage = new Stage();
        windowsRegistry.register(stage, this);
        return stage;
    }
}
