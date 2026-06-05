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
package com.treilhes.jfxplace.core.appmngr.action.impl;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationPrototype;
import com.treilhes.jfxplace.core.api.action.AbstractAction;
import com.treilhes.jfxplace.core.api.action.ActionExtensionFactory;
import com.treilhes.jfxplace.core.api.action.ActionMeta;
import com.treilhes.jfxplace.core.api.application.ActionFactory;
import com.treilhes.jfxplace.core.api.application.InstancesManager;
import com.treilhes.jfxplace.core.api.i18n.I18N;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstanceUi;
import com.treilhes.jfxplace.core.api.ui.dialog.Alert;
import com.treilhes.jfxplace.core.api.ui.dialog.Dialog;

@ApplicationPrototype("com.treilhes.jfxplace.core.appmngr.action.impl.CloseAllInstancesAction")
@ActionMeta(nameKey = "action.name.toggle.dock", descriptionKey = "action.description.toggle.dock")
public class CloseAllInstancesAction extends AbstractAction {

    private static final Logger logger = LoggerFactory.getLogger(CloseAllInstancesAction.class);

    public static final String MENU_ID = "fb462a53-9877-4f85-a37f-8aa07f6a3ec1";

    private final InstancesManager main;
    private final Dialog dialog;
    private final ActionFactory actionFactory;

    public CloseAllInstancesAction(
            I18N i18n,
            ActionExtensionFactory extensionFactory,
            ActionFactory actionFactory,
            InstancesManager main,
            Dialog dialog) {
        super(i18n, extensionFactory);
        this.main = main;
        this.dialog = dialog;
        this.actionFactory = actionFactory;
    }

    @Override
    public boolean canPerform() {
        return true;
    }

    @Override
    public ActionStatus doPerform() {

        // Check if an editing session is on going
        if (main.getInstances().stream().map(ApplicationInstance::getUi).anyMatch(ApplicationInstanceUi::isEditing)) {
            return ActionStatus.CANCELLED;
        }

        // Collects the documents with pending changes
        final var pendingDocs = main.getInstances().stream().filter(i -> i.getUi().isDocumentDirty())
                .collect(Collectors.toList());

        // Notifies the user if some documents are dirty
        final boolean exitConfirmed;
        switch (pendingDocs.size()) {
        case 0: {
            exitConfirmed = true;
            break;
        }

        case 1: {
            final ApplicationInstance instance = pendingDocs.get(0);
            var context = instance.getContext();
            var result = context.getBean(CloseInstanceAction.class).checkAndPerform();
            exitConfirmed = result == ActionStatus.DONE;
            break;
        }

        default: {
            assert pendingDocs.size() >= 2;

            final Alert d = dialog.customAlert();
            d.setMessage(getI18n().getString("alert.review.question.message", pendingDocs.size()));
            d.setDetails(getI18n().getString("alert.review.question.details"));

            var modalWindow = d.getModalWindow();
            modalWindow.setOKButtonTitle(getI18n().getString("label.review.changes"));
            modalWindow.setActionButtonTitle(getI18n().getString("label.discard.changes"));
            modalWindow.setActionButtonVisible(true);

            switch (d.showAndWait()) {
            default:
            case OK: { // Review
                int i = 0;
                ActionStatus status;
                do {
                    var instance = pendingDocs.get(i++);
                    var context = instance.getContext();
                    status = context.getBean(CloseInstanceAction.class).checkAndPerform();
                } while ((status == ActionStatus.DONE) && (i < pendingDocs.size()));
                exitConfirmed = (status == ActionStatus.DONE);
                break;
            }
            case CANCEL: {
                exitConfirmed = false;
                break;
            }
            case ACTION: { // Do not review
                exitConfirmed = true;
                break;
            }
            }
            break;
        }
        }

        // Exit if confirmed
        if (exitConfirmed) {
            main.close();

            // TODO (elp): something else here ?
//            logger.info(getI18n().getString("log.stop"));
//            Platform.exit();
        }


        return ActionStatus.DONE;
    }

}