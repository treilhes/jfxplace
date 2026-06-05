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
package com.treilhes.jfxplace.core.api;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.EmContextShutdownHooks;
import com.treilhes.emc4j.boot.api.context.beans.ExtensionDefinition;
import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.treilhes.emc4j.boot.api.loader.extension.RootExtension;
import com.treilhes.jfxplace.core.api.action.ActionExtensionFactory;
import com.treilhes.jfxplace.core.api.application.ApplicationClassloader;
import com.treilhes.jfxplace.core.api.i18n.I18N;
import com.treilhes.jfxplace.core.api.javafx.JfxPlaceExecutor;
import com.treilhes.jfxplace.core.api.javafx.internal.FxmlControllerBeanPostProcessor;
import com.treilhes.jfxplace.core.api.javafx.internal.JavaFxWindowManager;
import com.treilhes.jfxplace.core.api.javafx.internal.JavaFxWindowOwnerRegistryImpl;
import com.treilhes.jfxplace.core.api.javafx.internal.JavafxThreadBootstrapper;
import com.treilhes.jfxplace.core.api.javafx.internal.JavafxThreadClassloaderDispatcher;
import com.treilhes.jfxplace.core.api.job.JobExtensionFactory;
import com.treilhes.jfxplace.core.api.settings.MavenSetting;
import com.treilhes.jfxplace.core.api.subjects.ApplicationEvents;
import com.treilhes.jfxplace.core.api.subjects.ApplicationInstanceEvents;
import com.treilhes.jfxplace.core.api.subjects.DockManager;
import com.treilhes.jfxplace.core.api.subjects.LifecyclePostProcessor;
import com.treilhes.jfxplace.core.api.subjects.NetworkManager;
import com.treilhes.jfxplace.core.api.subjects.ViewManager;
import com.treilhes.jfxplace.core.api.task.TaskService;
import com.treilhes.jfxplace.core.api.ui.controller.dock.DockFactory;
import com.treilhes.jfxplace.core.api.ui.controller.dock.DockNameHelper;
import com.treilhes.jfxplace.core.api.ui.controller.dock.ViewController;
import com.treilhes.jfxplace.core.api.ui.controller.menu.MenuBuilder;
import com.treilhes.jfxplace.javafx.fxml.patch.PatchLink;

import javafx.application.Platform;


public class JfxplaceCoreApiExtension implements RootExtension {

    public static final UUID ID = ROOT_ID;

    /**
     * The UUID for the manager application extension.
     */
    public static final UUID MANAGER_APP_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    /**
     * The UUID for the tray application extension.
     */
    public static final UUID TRAY_APP_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");

    @Override
    public UUID getParentId() {
        return Extension.BOOT_ID;
    }

    @Override
    public UUID getId() {
        return ROOT_ID;
    }

    @Override
    public List<Class<?>> localContextClasses() {
        return Arrays.asList(
                //Selection.class,
                ActionExtensionFactory.class,
                com.treilhes.jfxplace.core.api.application.ActionFactory.class,
                com.treilhes.jfxplace.core.api.instance.ActionFactory.class,

                ApplicationEvents.ApplicationEventsImpl.class,
                ApplicationInstanceEvents.ApplicationInstanceEventsImpl.class,
                DockFactory.class,
                DockManager.DockManagerImpl.class,
                DockNameHelper.class,
                FxmlControllerBeanPostProcessor.class,
                I18N.class,
                JavafxThreadBootstrapper.class,
                JavaFxWindowManager.class,
                JavaFxWindowOwnerRegistryImpl.class,
                ApplicationClassloader.class,
                JavafxThreadClassloaderDispatcher.class,
                JfxPlaceExecutor.class,
                JobExtensionFactory.class,
                LifecyclePostProcessor.class,
                MavenSetting.class,
                MenuBuilder.class,
                NetworkManager.NetworkManagerImpl.class,
                TaskService.class,
                ViewController.class,
                ViewManager.ViewManagerImpl.class
        );
    }

    @Override
    public void initializeContext(EmContext context, EmContextShutdownHooks shutdownHooks) {
        shutdownHooks.registerInheritedShutdownHook(c -> {
            var extensionDef = c.getLocalBean(ExtensionDefinition.class);
            var extension = extensionDef.getExtension();
            PatchLink.cleanBeanAdapterGlobalCache(extension.getClass().getModule().getLayer());
            Platform.runLater(() -> Thread.currentThread().setContextClassLoader(null));
        });

        shutdownHooks.registerShutdownHook(c -> System.out.println("Shutdown of " + c.getId()));
    }

    @Override
    public void finalizeContext(EmContext context) {

    }


}
