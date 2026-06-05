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
package com.treilhes.jfxplace.app.debugtools.app.ui.view;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.emc4j.boot.api.context.ContextManager;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.emc4j.boot.api.layer.ModuleLayerManager;
import com.treilhes.emc4j.boot.api.web.client.InternalRestClient;
import com.treilhes.jfxplace.app.debugtools.api.events.DebugEvents;
import com.treilhes.jfxplace.app.debugtools.api.ui.Docks;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.javafx.LoadInFxThread;
import com.treilhes.jfxplace.core.api.ui.controller.AbstractFxmlViewController;
import com.treilhes.jfxplace.core.api.ui.controller.dock.annotation.ViewAttachment;
import com.treilhes.jfxplace.core.api.ui.controller.menu.ViewMenu;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

// @formatter:off
@ApplicationInstanceSingleton
@ViewAttachment(
        name = "OpenApis",
        id = "88e8cd7f-9030-4cb0-bd0b-d78add0d4a0c",
        prefDockId = Docks.CENTER_DOCK_ID,
        openOnStart = true,
        selectOnStart = false,
        order = 4000,
        icon = "openapi_tool.png",
        iconX2 = "openapi_tool@2x.png"
        )
@LoadInFxThread
// @formatter:on
public class OpenApiController extends AbstractFxmlViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiController.class);
    private static final String OPENAPI_UI_PATH = "swagger-ui.html";

    @FXML
    private Label noContextLabel;

    @FXML
    private WebView webView;

    private final ApplicationInstance instance;
    private final InternalRestClient restClient;
    private final DebugEvents debugEvents;

    //@formatter:off
    protected OpenApiController(
            ApplicationInstance instance,
            ViewMenu viewMenu,
            DebugEvents debugEvents,
            InternalRestClient restClient,
            ModuleLayerManager moduleLayerManager,
            ContextManager contextManager) {
        //@formatter:on
        super(instance.getApplication().getI18n(), instance.getApplication().getEvents(), instance.getEvents(), viewMenu, OpenApiController.class.getResource("OpenApi.fxml"));
        this.instance = instance;
        this.restClient = restClient;
        this.debugEvents = debugEvents;
    }

    @FXML
    public void initialize() {
        debugEvents.context().subscribe(c -> loadApi());
    }


    @Override
    public void controllerDidLoadFxml() {
        getRoot().setId(OpenApiController.class.getSimpleName());
        getRoot().minWidth(400.0);
        getRoot().minHeight(400.0);
    }

    @Override
    public void onShow() {
        loadApi();
    }

    @Override
    public void onHidden() {
        // TODO Auto-generated method stub
    }

    private void loadApi() {

        var context = debugEvents.context().get();

        var hasContext = context != null;

        instance.getExecutor().runOnFxThread(() -> {
            noContextLabel.setVisible(!hasContext);
            webView.setVisible(hasContext);

            if(!hasContext) {
                webView.getEngine().load(null);
                return;
            }

            var apiUri = getUri(context.getUuid());
            if(apiUri != null) {
                webView.getEngine().load(apiUri.toString());
            } else {
                webView.getEngine().load(null);
            }
        });

    }

    private URI getUri(UUID layerId) {
        try {
            return restClient.get(layerId, OPENAPI_UI_PATH).getUri();
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Error while fetching OpenAPI UI URI of " + layerId, e);
            return null;
        }
    }
}
