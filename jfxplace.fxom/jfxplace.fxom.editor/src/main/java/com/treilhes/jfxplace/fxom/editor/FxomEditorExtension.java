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
package com.treilhes.jfxplace.fxom.editor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.gluonhq.jfxapps.core.api.fxom.ui.tool.NoPickRefiner;
import com.treilhes.emc4j.boot.api.loader.extension.OpenExtension;
import com.treilhes.jfxplace.fxom.editor.controller.ContentPanelController;
import com.treilhes.jfxplace.fxom.editor.controller.ModeManagerController;
import com.treilhes.jfxplace.fxom.editor.controller.WorkspaceController;
import com.treilhes.jfxplace.fxom.editor.editor.messagelog.MessageLog;
import com.treilhes.jfxplace.fxom.editor.i18n.I18NLayout;
import com.treilhes.jfxplace.fxom.editor.message.MessageBarController;
import com.treilhes.jfxplace.fxom.editor.message.MessagePanelController;
import com.treilhes.jfxplace.fxom.editor.message.MessagePopupController;
import com.treilhes.jfxplace.fxom.editor.preference.BackgroundImagePreference;
import com.treilhes.jfxplace.fxom.editor.selectionbar.SelectionBarController;
import com.treilhes.jfxplace.fxom.editor.tool.DriverExtensionRegistryImpl;
import com.treilhes.jfxplace.fxom.editor.tool.GenericDriver;

public class FxomEditorExtension implements OpenExtension {

    public static final UUID ID = UUID.fromString("51c14d5d-1f38-4f15-ae5d-c7d493d4e726");

    @Override
    public UUID getId() {
        return ID;
    }

    @Override
    public UUID getParentId() {
        return OpenExtension.ROOT_ID;
    }

    @Override
    public List<Class<?>> exportedContextClasses() {
        // @formatter:off
        return Arrays.asList(

                //EditorInstancesController.class,
                //EditorsManagerImpl.class,
                //temp EditorController.class,
                BackgroundImagePreference.class,
                ContentPanelController.class,
                DriverExtensionRegistryImpl.class,
                GenericDriver.class,
                I18NLayout.class,
                MessageBarController.class,
                MessageLog.class,
                MessagePanelController.class,
                MessagePopupController.class,
                ModeManagerController.class,
                NoPickRefiner.class,
                SelectionBarController.class,
                WorkspaceController.class

                );
        // @formatter:on
    }

    @Override
    public List<Class<?>> localContextClasses() {
        return List.of();
    }


}
