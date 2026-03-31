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
package com.treilhes.jfxplace.core.ui;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.treilhes.emc4j.boot.api.loader.extension.OpenExtension;
import com.treilhes.jfxplace.core.ui.controller.ApplicationInstanceController;
import com.treilhes.jfxplace.core.ui.controller.ApplicationWindowTracker;
import com.treilhes.jfxplace.core.ui.dialog.ModalWindowImpl;
import com.treilhes.jfxplace.core.ui.dialog.application.ApplicationDialogController;
import com.treilhes.jfxplace.core.ui.dialog.application.ApplicationMessageController;
import com.treilhes.jfxplace.core.ui.dialog.application.ApplicationMessageDialog;
import com.treilhes.jfxplace.core.ui.dialog.instance.AlertDialog;
import com.treilhes.jfxplace.core.ui.dialog.instance.DialogController;
import com.treilhes.jfxplace.core.ui.dialog.instance.ErrorDialog;
import com.treilhes.jfxplace.core.ui.dialog.instance.TextViewDialog;
import com.treilhes.jfxplace.core.ui.dock.AnnotatedViewAttachmentProvider;
import com.treilhes.jfxplace.core.ui.dock.DockPanelController;
import com.treilhes.jfxplace.core.ui.dock.DockViewControllerImpl;
import com.treilhes.jfxplace.core.ui.dock.DockWindowController;
import com.treilhes.jfxplace.core.ui.dock.DockWindowFactory;
import com.treilhes.jfxplace.core.ui.dock.action.DockActionFactoryImpl;
import com.treilhes.jfxplace.core.ui.dock.action.impl.ChangeDockTypeAction;
import com.treilhes.jfxplace.core.ui.dock.action.impl.CloseDockAction;
import com.treilhes.jfxplace.core.ui.dock.action.impl.CloseViewAction;
import com.treilhes.jfxplace.core.ui.dock.action.impl.MoveToDockAction;
import com.treilhes.jfxplace.core.ui.dock.action.impl.ToggleMinimizeDockAction;
import com.treilhes.jfxplace.core.ui.dock.action.impl.ToggleViewVisibilityAction;
import com.treilhes.jfxplace.core.ui.dock.action.impl.UndockViewAction;
import com.treilhes.jfxplace.core.ui.dock.preference.DockMinimizedPreference;
import com.treilhes.jfxplace.core.ui.dock.preference.LastDockDockTypePreference;
import com.treilhes.jfxplace.core.ui.dock.preference.LastDockUuidPreference;
import com.treilhes.jfxplace.core.ui.dock.preference.LastViewVisibilityPreference;
import com.treilhes.jfxplace.core.ui.dock.type.DockTypeAccordion;
import com.treilhes.jfxplace.core.ui.dock.type.DockTypeLatestOnly;
import com.treilhes.jfxplace.core.ui.dock.type.DockTypeSplitH;
import com.treilhes.jfxplace.core.ui.dock.type.DockTypeSplitV;
import com.treilhes.jfxplace.core.ui.dock.type.DockTypeTab;
import com.treilhes.jfxplace.core.ui.i18n.I18NLayout;
import com.treilhes.jfxplace.core.ui.inlineedit.InlineEditController;
import com.treilhes.jfxplace.core.ui.preference.MaximizedPreference;
import com.treilhes.jfxplace.core.ui.preference.StageHeightPreference;
import com.treilhes.jfxplace.core.ui.preference.StageWidthPreference;
import com.treilhes.jfxplace.core.ui.preference.XPosPreference;
import com.treilhes.jfxplace.core.ui.preference.YPosPreference;
import com.treilhes.jfxplace.core.ui.viewlinks.ViewLinksController;

public class BaseUiExtension implements OpenExtension {

    public static final UUID ID = UUID.fromString("cc8b28e8-b070-4cbd-8558-eff205a28cf1");

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
                AlertDialog.class,
                AnnotatedViewAttachmentProvider.class,
                ApplicationInstanceController.class,
                ApplicationWindowTracker.class,
                ApplicationDialogController.class,
                ApplicationMessageDialog.class,
                ApplicationMessageController.class,
                ChangeDockTypeAction.class,
                CloseDockAction.class,
                CloseViewAction.class,
                DialogController.class,
                DockActionFactoryImpl.class,
                DockMinimizedPreference.class,
                DockPanelController.class,
                DockTypeAccordion.class,
                DockTypeSplitH.class,
                DockTypeSplitV.class,
                DockTypeTab.class,
                DockTypeLatestOnly.class,
                DockViewControllerImpl.class,
                DockWindowController.class,
                DockWindowFactory.class,
                ErrorDialog.class,
                I18NLayout.class,
                InlineEditController.class,
                LastDockDockTypePreference.class,
                LastDockUuidPreference.class,
                LastViewVisibilityPreference.class,
                MaximizedPreference.class,
                ModalWindowImpl.class,
                MoveToDockAction.class,
                StageHeightPreference.class,
                StageWidthPreference.class,
                TextViewDialog.class,
                ToggleMinimizeDockAction.class,
                ToggleViewVisibilityAction.class,
                UndockViewAction.class,
                ViewLinksController.class,
                XPosPreference.class,
                YPosPreference.class

                );
        // @formatter:on
    }

    @Override
    public List<Class<?>> localContextClasses() {
        return List.of();
    }


}
