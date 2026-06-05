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
package com.treilhes.jfxplace.core.maven.artifact;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;

import com.treilhes.emc4j.test.EmcInject;
import com.treilhes.emc4j.test.EmcInjectMock;
import com.treilhes.jfxplace.core.api.maven.MavenClient;
import com.treilhes.jfxplace.core.api.settings.MavenSetting;
import com.treilhes.jfxplace.core.api.ui.MainInstanceWindow;
import com.treilhes.jfxplace.core.api.ui.controller.misc.IconSetting;
import com.treilhes.jfxplace.core.api.ui.controller.misc.MessageLogger;
import com.treilhes.jfxplace.core.maven.preference.MavenRepositoriesPreferences;
import com.treilhes.jfxplace.test.JfxPlaceTest;
import com.treilhes.jfxplace.test.builder.StageBuilder;
import com.treilhes.jfxplace.test.builder.StageType;

@JfxPlaceTest(classes = {GetMavenArtifactDialogController.class})
class GetMavenArtifactDialogControllerTest {

    @EmcInjectMock
    MavenClient mc;

    @EmcInjectMock
    IconSetting iconSetting;

    @EmcInjectMock
    MessageLogger messageLogger;

    @EmcInjectMock
    MavenSetting mavenSetting;

    @EmcInjectMock
    MavenRepositoriesPreferences repositoryPreferences;

    @EmcInjectMock
    MainInstanceWindow mainWindow;

    @EmcInject
    StageBuilder builder;

    @Test
    void should_load_the_fxml() {
        try (var testStage = builder.controller(GetMavenArtifactDialogController.class).show()) {
            assertNotNull(testStage.getController().getRoot());
        }
    }

    @Test
    void show_ui(FxRobot robot) {
        try (var testStage = builder
            .controller(GetMavenArtifactDialogController.class)
            .setup(StageType.None)
            .size(600, 800)
            .show()){

            Mockito.when(mainWindow.getStage()).thenReturn(testStage.getStage());

            var controller = testStage.getController();

            robot.interact(() -> controller.openWindow(null));

            assertTrue(controller.isOpen());

            robot.interact(controller::closeWindow);

            assertFalse(controller.isOpen());
        }
    }

}
