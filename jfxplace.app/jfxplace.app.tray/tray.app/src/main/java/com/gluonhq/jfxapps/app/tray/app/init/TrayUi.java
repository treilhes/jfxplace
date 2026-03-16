/*
 * Copyright (c) 2016, 2024, Gluon and/or its affiliates.
 * Copyright (c) 2021, 2024, Pascal Treilhes and/or its affiliates.
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
package com.gluonhq.jfxapps.app.tray.app.init;

import java.awt.event.ActionEvent;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gluonhq.jfxapps.app.tray.app.utils.SystemTrayJavaFxProvider;
import com.gluonhq.jfxapps.core.api.JfxplaceCoreApiExtension;
import com.gluonhq.jfxapps.core.api.ui.MainInstanceWindow;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.loader.ApplicationManager;
import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;

import dorkbox.jna.rendering.RenderProvider;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.CacheUtil;
import jakarta.annotation.PostConstruct;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

@ApplicationSingleton
public class TrayUi implements MainInstanceWindow {
    private static final Logger logger = LoggerFactory.getLogger(TrayUi.class);
    private static final boolean DEBUG = true;

    private final ApplicationManager applicationManager;

    private Stage stage;
    private Scene scene;


    // @formatter:off
    public TrayUi(
            ApplicationManager applicationManager
            ) {
     // @formatter:on
        super();
        this.applicationManager = applicationManager;
    }

    @PostConstruct
    public void initialize() {
        Platform.runLater(() -> addTrayIcon());
    }

    private void addTrayIcon() {
        logger.info("Adding system tray icon...");

        try {
            RenderProvider.set(new SystemTrayJavaFxProvider());

            SystemTray.DEBUG = DEBUG; // for test apps, we always want to run in debug mode

            // for test apps, make sure the cache is always reset. These are the ones used, and you should never do this in production.
            new CacheUtil("SysTrayExample").clear();

         // Get a SystemTray instance (auto-detects native or fallback)

            SystemTray systemTray = SystemTray.get("SysTrayExample");

            if (systemTray == null) {
                System.err.println("Unable to load SystemTray!");
                return;
            }

            var iconUrl = this.getClass().getResource("icon.png");//.toString();
            // Set the image/icon for the tray
            systemTray.setImage(iconUrl);

            // Optional: set a status text
            systemTray.setStatus("App Running");

            // Add some menu items
            systemTray.getMenu().add(new MenuItem("Manage", (ActionEvent e) -> {
                var mngrId = JfxplaceCoreApiExtension.MANAGER_APP_ID;
                applicationManager.startApplication(mngrId);
                applicationManager.send(new OpenCommandEvent(mngrId, List.of()));
            }));

            systemTray.getMenu().add(new MenuItem("Force Quit", (ActionEvent e) -> {
                systemTray.shutdown();  // removes the tray icon
                System.exit(0);
            }));

        } catch (Exception e) {
            logger.error("Error while adding system tray icon", e);
        }
    }

    /**
     * Returns the scene of this window. This method invokes {@link #getRoot()}.
     * When called the first time, it also invokes
     * {@link #controllerDidCreateScene()} just after creating the scene object.
     *
     * @return the scene object of this window (never null)
     */
    @Override
    public Scene getScene() {
        assert Platform.isFxApplicationThread();

        if (scene == null) {
            scene = new Scene(new Label("Hello"));
        }

        return scene;
    }

    /**
     * Returns the stage of this window. This method invokes {@link #getScene()}.
     * When called the first time, it also invokes
     * {@link #controllerDidCreateStage()} just after creating the stage object.
     *
     * @return the stage object of this window (never null).
     */
    public Stage getStage(boolean renew) {
        assert Platform.isFxApplicationThread();

        if (stage == null || renew) {
            stage = new Stage();
            //stage.initOwner(this.owner == null ? null : this.owner.getStage());
            //stage.setOnCloseRequest(closeRequestHandler);
            //stage.focusedProperty().addListener(focusEventHandler);
            stage.setScene(getScene());
            //clampWindow();
            //if (sizeToScene) {
            //    stage.sizeToScene();
            //}
            // By default we set the same icons as the owner
            //if (this.owner != null) {
            //    stage.getIcons().addAll(this.owner.getStage().getIcons());
            //} else if (iconSetting != null) {
            //    iconSetting.setWindowIcon(stage);
            //}

            //controllerDidCreateStage();
        }

        return stage;
    }

    @Override
    public Stage getStage() {
        return getStage(false);
    }

    @Override
    public void closeWindow() {
        // TODO Auto-generated method stub

    }

    @Override
    public void openWindow() {

    }

    @Override
    public void setCloseHandler(CloseHandler closeHandler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFocusHandler(FocusHandler closeHandler) {
        // TODO Auto-generated method stub

    }


    @Override
    public void setMainKeyPressedEvent(EventHandler<KeyEvent> mainKeyEventFilter) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateStageTitle() {
        // TODO Auto-generated method stub

    }

    @Override
    public void composeWindow() {
        // TODO Auto-generated method stub

    }

}
