/*
 * Copyright (c) 2016, 2026, Gluon and/or its affiliates.
 * Copyright (c) 2021, 2026, Pascal Treilhes and/or its affiliates.
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
package com.treilhes.jfxplace.fxom.sampledata.action;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstancePrototype;
import com.treilhes.jfxplace.core.api.action.AbstractAction;
import com.treilhes.jfxplace.core.api.action.ActionExtensionFactory;
import com.treilhes.jfxplace.core.api.action.ActionMeta;
import com.treilhes.jfxplace.core.api.fxom.subjects.FxomEvents;
import com.treilhes.jfxplace.core.api.i18n.I18N;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPipeline;
import com.treilhes.jfxplace.core.fxom.sample.SampleDataEnabledPreference;

@ApplicationInstancePrototype("com.treilhes.jfxplace.fxom.sampledata.action.ToggleSampleDataAction")
@ActionMeta(nameKey = "action.name.toggle.dock", descriptionKey = "action.description.toggle.dock")
public class ToggleSampleDataAction extends AbstractAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(ToggleSampleDataAction.class);

    private final FxomEvents fxomEvents;
    private final FXOMPipeline fxomPipeline;
    private final SampleDataEnabledPreference sampleDataEnabledPreference;

    //@formatter:off
    public ToggleSampleDataAction(
            I18N i18n,
            ActionExtensionFactory extensionFactory,
            FxomEvents fxomEvents,
            FXOMPipeline fxomPipeline,
            SampleDataEnabledPreference sampleDataEnabledPreference) {
        //@formatter:on
        super(i18n, extensionFactory);
        this.fxomEvents = fxomEvents;
        this.fxomPipeline = fxomPipeline;
        this.sampleDataEnabledPreference = sampleDataEnabledPreference;
    }

    @Override
    public boolean canPerform() {
        return fxomEvents.fxomDocument().get() != null;
    }

    @Override
    public ActionStatus doPerform() {
        var value = sampleDataEnabledPreference.getValue();
        sampleDataEnabledPreference.setValue(!value);

        var document = fxomEvents.fxomDocument().get();
        try {
            var newDocument = fxomPipeline.clone(document);
            fxomEvents.fxomDocument().set(newDocument);
            return ActionStatus.DONE;
        } catch (IOException e) {
            LOGGER.error("Failed to toggle sample data", e);
            return ActionStatus.FAILED;
        }
    }

}