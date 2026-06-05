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
package com.treilhes.jfxplace.core.ui.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.DirtiesContext;
import org.testfx.api.FxRobot;

import com.treilhes.emc4j.test.EmcInject;
import com.treilhes.emc4j.test.EmcInjectMock;
import com.treilhes.jfxplace.core.api.ctxmenu.ContextMenu;
import com.treilhes.jfxplace.core.api.driver.Driver;
import com.treilhes.jfxplace.core.api.fxom.content.mode.ModeManager;
import com.treilhes.jfxplace.core.api.fxom.mask.FXOMObjectMask;
import com.treilhes.jfxplace.core.api.fxom.subjects.FxomEvents;
import com.treilhes.jfxplace.core.api.fxom.ui.controller.misc.Content;
import com.treilhes.jfxplace.core.api.selection.Selection;
import com.treilhes.jfxplace.core.api.subjects.ApplicationEvents;
import com.treilhes.jfxplace.core.api.tooltheme.ToolStylesheetProvider;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.fxom.editor.controller.WorkspaceController;
import com.treilhes.jfxplace.fxom.editor.preference.BackgroundImagePreference;
import com.treilhes.jfxplace.fxom.editor.preference.BackgroundImagePreference.BackgroundImage;
import com.treilhes.jfxplace.test.JfxPlaceTest;
import com.treilhes.jfxplace.test.builder.StageBuilder;
import com.treilhes.jfxplace.test.builder.StageType;
import com.treilhes.jfxplace.util.URLUtils;

import io.reactivex.rxjava3.subjects.PublishSubject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;

@JfxPlaceTest(classes = { WorkspaceController.class, FXOMObjectMask.Factory.class })
class WorkspaceControllerTest {

    private static final String WORKSPACE_CSS = """
            #scrollPane {
                -fx-border-color: red;
            }
            #workspacePane {
                -fx-background-color: green;
            }
            """;

    @EmcInjectMock
    ContextMenu contextMenu;

    @EmcInjectMock
    BackgroundImagePreference backgroundImagePreference;

    @EmcInjectMock
    Selection selection;

    @EmcInjectMock
    Content content;

    @EmcInjectMock
    ModeManager nodeManager;

    @EmcInjectMock
    Driver driver;

    @EmcInject
    ApplicationEvents applicationEvents;

    @EmcInject
    FxomEvents instanceEvents;

    @EmcInject
    StageBuilder builder;

    @Test
    @DirtiesContext
    void should_load_the_fxml() {
        Mockito.when(backgroundImagePreference.getObservableValue())
        .thenReturn(new SimpleObjectProperty<>(BackgroundImage.BACKGROUND_01));
        Mockito.when(content.contentChanged()).thenReturn(PublishSubject.create());

        try (var testStage = builder.controller(WorkspaceController.class).show()) {
            assertNotNull(testStage.getController().getRoot());
        }
    }

    @Test
    @DirtiesContext
    void show_ui(FxRobot robot) {

        Mockito.when(backgroundImagePreference.getObservableValue())
        .thenReturn(new SimpleObjectProperty<>(BackgroundImage.BACKGROUND_01));
        Mockito.when(content.contentChanged()).thenReturn(PublishSubject.create());

        try ( var testStage = builder.controller(WorkspaceController.class)
                .size(800, 600)
                .setup(StageType.Fill)
                .css(WORKSPACE_CSS)
                .show()) {

            applicationEvents.stylesheetConfig().set(ToolStylesheetProvider.builder()
                    .stylesheet(URLUtils.toDataURI("""
                            #scrollPane {
                                -fx-border-color: green;
                            }
                            #workspacePane {
                                -fx-background-color: blue;
                            }
                            """).toString())
                    .build());
        }
    }

    @Test
    @DirtiesContext
    void should_show_document_null_label(FxRobot robot) throws Exception {

        var contentChanged = PublishSubject.<Boolean>create();

        Mockito.when(backgroundImagePreference.getObservableValue())
        .thenReturn(new SimpleObjectProperty<>(BackgroundImage.BACKGROUND_01));

        Mockito.when(content.contentChanged()).thenReturn(contentChanged);

        try (var testStage = builder.controller(WorkspaceController.class)
                .size(800, 600)
                .setup(StageType.Fill)
                .css(WORKSPACE_CSS)
                .show()) {

            var workspace = testStage.getController();

            Mockito.when(content.hasContent()).thenReturn(false);

            robot.interact(() -> contentChanged.onNext(true));

            // lookup from workspace to ensure we get current test backgroundPane
            Label backgroundPane = robot.from(workspace.getRoot()).lookup("#backgroundPane").query();

            assertEquals("FXOMDocument is null", backgroundPane.getText());
        }
    }

    @Test
    @DirtiesContext
    void should_show_undisplayable_document_label(FxRobot robot) throws IOException {

        var contentChanged = PublishSubject.<Boolean>create();

        Mockito.when(backgroundImagePreference.getObservableValue())
        .thenReturn(new SimpleObjectProperty<>(BackgroundImage.BACKGROUND_01));
        Mockito.when(content.contentChanged()).thenReturn(contentChanged);


        try (var testStage = builder.controller(WorkspaceController.class)
                .size(800, 600)
                .setup(StageType.Fill)
                .css(WORKSPACE_CSS)
                .show()) {

            var workspace = testStage.getController();

            Mockito.when(content.hasContent()).thenReturn(true);
            Mockito.when(content.isDisplayable()).thenReturn(false);

            robot.interact(() -> {
                contentChanged.onNext(true);
            });

            // lookup from workspace to ensure we get current test backgroundPane
            Label backgroundPane = robot.from(workspace.getRoot()).lookup("#backgroundPane").query();

            assertEquals("content.label.status.invitation", backgroundPane.getText());
        }

    }

    @Test
    @DirtiesContext
    void should_scale_the_content(FxRobot robot) throws IOException {

        var contentChanged = PublishSubject.<Boolean>create();

        Mockito.when(backgroundImagePreference.getObservableValue())
        .thenReturn(new SimpleObjectProperty<>(BackgroundImage.BACKGROUND_01));
        Mockito.when(content.contentChanged()).thenReturn(contentChanged);

        try (var testStage = builder.controller(WorkspaceController.class)
                .size(800, 600)
                .setup(StageType.Fill)
                .document("""
                        <?import javafx.scene.Scene?>
                        <?import javafx.scene.layout.AnchorPane?>
                        <?import javafx.stage.Stage?>

                        <Stage xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1">
                            <scene>
                                <Scene>
                                    <AnchorPane prefHeight="200" prefWidth="200" />
                                </Scene>
                            </scene>
                        </Stage>
                        """)
                .css(WORKSPACE_CSS)
                .show()) {

            var workspace = testStage.getController();

            FXOMDocument fxomDocument = instanceEvents.fxomDocument().get();

            Mockito.when(content.hasContent()).thenReturn(true);
            Mockito.when(content.isDisplayable()).thenReturn(true);
            Mockito.when(content.getRoot()).thenReturn(fxomDocument.getDisplayNodeOrSceneGraphRoot());

            robot.interact(() -> {
                contentChanged.onNext(true);
            });

            Node sceneGraph = (Node)fxomDocument.getDisplayNodeOrSceneGraphRoot();

            var before = sceneGraph.localToScreen(sceneGraph.getLayoutBounds());

            robot.interact(() -> {
                workspace.setScaling(2.0d);
            });

            var after = sceneGraph.localToScreen(sceneGraph.getLayoutBounds());

            assertEquals(before.getWidth()*2, after.getWidth(), 0.1);
            assertEquals(before.getHeight()*2, after.getHeight(), 0.1);
        }
    }
}
