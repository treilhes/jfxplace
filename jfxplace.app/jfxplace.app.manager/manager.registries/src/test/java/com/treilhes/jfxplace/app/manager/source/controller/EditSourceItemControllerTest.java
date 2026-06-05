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
package com.treilhes.jfxplace.app.manager.source.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

import com.treilhes.emc4j.boot.api.maven.RepositoryClient;
import com.treilhes.emc4j.boot.api.registry.RegistryArtifactManager;
import com.treilhes.emc4j.test.EmcInject;
import com.treilhes.emc4j.test.EmcInjectMock;
import com.treilhes.jfxplace.app.manager.api.ManagerApiExtension;
import com.treilhes.jfxplace.app.manager.registries.controller.EditSourceItemController;
import com.treilhes.jfxplace.app.manager.registries.model.SourceModelController;
import com.treilhes.jfxplace.core.api.ui.controller.menu.ViewMenu;
import com.treilhes.jfxplace.test.JfxPlaceTest;
import com.treilhes.jfxplace.test.builder.StageBuilder;
import com.treilhes.jfxplace.test.builder.StageType;

import javafx.scene.control.ComboBox;

@JfxPlaceTest(classes = { EditSourceItemController.class, SourceModelController.class })
class EditSourceItemControllerTest {

    @EmcInjectMock
    ViewMenu viewMenu;

    @EmcInjectMock
    RegistryArtifactManager registryArtifactManager;

    @EmcInjectMock
    RepositoryClient mavenClient;

    @EmcInject
    StageBuilder stageBuilder;

    @Test
    void should_load_the_fxml() {
        try(var testStage = stageBuilder.controller(EditSourceItemController.class).show()){
            assertNotNull(testStage.getController().getRoot());
        }
    }

    @Test
    void validate_and_version_must_be_disabled_if_groupid_and_artifactid_are_empty(FxRobot robot) {
        try (var testStage = stageBuilder.controller(EditSourceItemController.class).size(800, 400)
                .css(ManagerApiExtension.class.getResource("/com/treilhes/jfxplace/app/manager/api/ui/Manager.css"))
                .setup(StageType.Fill).show()) {

            var controller = testStage.getController();

            addTestBackground(robot, controller);

            var validate = robot.lookup("#validate").queryButton();
            var cancel = robot.lookup("#cancel").queryButton();
            var versions = robot.lookup("#version").queryAs(ComboBox.class);

            var groupId = robot.lookup("#groupId").queryTextInputControl();
            var artifactId = robot.lookup("#artifactId").queryTextInputControl();

            assertTrue(validate.isDisabled());
            assertTrue(versions.isDisabled());
            assertFalse(cancel.isDisabled());

            robot.interact(() -> groupId.setText("xxxxxxxxx"));

            assertTrue(validate.isDisabled());
            assertTrue(versions.isDisabled());
            assertFalse(cancel.isDisabled());

            robot.interact(() -> artifactId.setText("yyyyyyyyy"));

            assertFalse(validate.isDisabled());
            assertFalse(versions.isDisabled());
            assertFalse(cancel.isDisabled());

        }

    }

    private void addTestBackground(FxRobot robot, EditSourceItemController controller) {
        robot.interact(() -> controller.getRoot().getScene().getRoot().setStyle(
                "-fx-background-color:  radial-gradient(focus-angle 0deg , focus-distance -80% , center 0% -10% , radius 100% , #d5e3e6 30%, #72adaa 80%, #293950)"));
    }
}
