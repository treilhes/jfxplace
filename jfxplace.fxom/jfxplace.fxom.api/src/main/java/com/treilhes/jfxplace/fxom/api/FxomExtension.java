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
package com.treilhes.jfxplace.fxom.api;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.treilhes.emc4j.boot.api.layer.Layer;
import com.treilhes.emc4j.boot.api.loader.extension.SealedExtension;
import com.treilhes.jfxplace.core.api.driver.DefaultDriverRegistry;
import com.treilhes.jfxplace.core.api.driver.GenericDriver;
import com.treilhes.jfxplace.core.api.fxom.editor.selection.DefaultSelectionGroupFactory;
import com.treilhes.jfxplace.core.api.fxom.editor.selection.ObjectSelectionGroup;
import com.treilhes.jfxplace.core.api.fxom.editor.selection.SelectionGroupFactoryRegistry;
import com.treilhes.jfxplace.core.api.fxom.gesture.DiscardGesture;
import com.treilhes.jfxplace.core.api.fxom.subjects.FxomEvents;
import com.treilhes.jfxplace.core.api.fxom.ui.tool.NoPickRefiner;

// FIXME this isn't really a RootExtension, but we need it to be initialized very early to apply the necessary module patches
// the way it must be loaded should be reconsidered
public class FxomExtension implements SealedExtension {

    public static final UUID ID = UUID.fromString("1619a4bc-e5f7-413a-a93e-eae379adf56b");

    @Override
    public void initializeModule(Layer layer) {
        var module = this.getClass().getModule();
        var fxomModule = module.getLayer().findModule("jfxplace.fxom.api").get();
        com.treilhes.jfxplace.javafx.fxml.patch.PatchLink.addOpen(fxomModule, "com.sun.javafx.fxml");
    }

    /**
     * The extension is a mergeable extension, so it doesn't have a parent.
     * @return null
     */
    @Override
    public UUID getParentId() {
        return null;
    }

    @Override
    public UUID getId() {
        return ID;
    }

    @Override
    public Set<UUID> getMergedExtensions() {
        return Set.of(
                UUID.fromString("a07b94ba-fdc0-4f4b-9b4c-10710128a45e"), //FxomModelExtension
                UUID.fromString("8e362a08-926c-4732-9e89-bdb5b0e87662"), //FxomMetadataExtension
                UUID.fromString("94124a19-94ad-49b4-84a1-a621db188b6f")  //FxomEditorsExtension
                );
    }

    @Override
    public List<Class<?>> localContextClasses() {
        return Arrays.asList(
                GenericDriver.class,
                DefaultDriverRegistry.class,
                FxomEvents.FxomEventsImpl.class,
                DefaultSelectionGroupFactory.class,
                DiscardGesture.Factory.class,
                DiscardGesture.class,
                NoPickRefiner.class,
                ObjectSelectionGroup.Factory.class,
                SelectionGroupFactoryRegistry.class
        );
    }
}
