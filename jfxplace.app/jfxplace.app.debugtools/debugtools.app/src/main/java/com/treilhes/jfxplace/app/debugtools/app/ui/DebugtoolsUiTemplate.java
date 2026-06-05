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
package com.treilhes.jfxplace.app.debugtools.app.ui;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.app.debugtools.api.ui.Docks;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.ui.MainInstanceWindow;
import com.treilhes.jfxplace.core.api.ui.controller.AbstractFxmlWindowController;
import com.treilhes.jfxplace.core.api.ui.controller.dock.Dock;
import com.treilhes.jfxplace.core.api.ui.controller.dock.Dock.Orientation;
import com.treilhes.jfxplace.core.api.ui.controller.dock.DockFactory;
import com.treilhes.jfxplace.core.api.ui.controller.menu.MenuBar;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

@ApplicationInstanceSingleton
public class DebugtoolsUiTemplate extends AbstractFxmlWindowController implements MainInstanceWindow {

    private final MenuBar menuBar;

    private final Dock leftDock;
    private final Dock centerDock;
    private final Dock rightDock;
    private final Dock bottomDock;

    @FXML
    private VBox leftHost;
    @FXML
    private VBox rightHost;
    @FXML
    private AnchorPane centerHost;
    @FXML
    private VBox bottomHost;

    // @formatter:off
    public DebugtoolsUiTemplate(
            ApplicationInstance instance,
            DockFactory dockFactory,
            MenuBar menuBar) {
        super(instance, DebugtoolsUiTemplate.class.getResource("DebugtoolsUiTemplate.fxml"), false);
        // @formatter:on

        this.menuBar = menuBar;
        this.leftDock = dockFactory.create(Docks.LEFT_DOCK_UUID, "Left");
        this.centerDock = dockFactory.create(Docks.CENTER_DOCK_UUID, "Center");
        this.rightDock = dockFactory.create(Docks.RIGHT_DOCK_UUID, "Right");
        this.bottomDock = dockFactory.create(Docks.BOTTOM_DOCK_UUID, "Bottom");
    }

    @FXML
    public void initialize() {
        this.bottomDock.setMinimizedOrientation(Orientation.HORIZONTAL);
    }

    @Override
    public void controllerDidLoadFxml() {
        super.controllerDidLoadFxml();
        assert getRoot() instanceof VBox;
    }


    @Override
    public void composeWindow() {
        final VBox rootVBox = (VBox) getRoot();
        rootVBox.getChildren().add(0, menuBar.getMenuBar());

        var content = centerDock.getContent();
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);

        centerHost.getChildren().add(content);
        leftHost.getChildren().add(leftDock.getContent());
        rightHost.getChildren().add(rightDock.getContent());
        bottomHost.getChildren().add(bottomDock.getContent());
    }

    @Override
    public void setMainKeyPressedEvent(EventHandler<KeyEvent> mainKeyEventFilter) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateStageTitle() {
        // TODO Auto-generated method stub

    }

}
