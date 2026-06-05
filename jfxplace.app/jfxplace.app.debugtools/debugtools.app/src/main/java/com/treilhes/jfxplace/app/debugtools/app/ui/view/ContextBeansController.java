/*
 * Copyright (c) 2021, 2024, Pascal Treilhes and/or its affiliates.
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
 *  - Neither the name of Pascal Treilhes nor the names of its
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

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationConfiguration;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstancePrototype;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationPrototype;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.context.annotation.DeportedSingleton;
import com.treilhes.emc4j.boot.api.context.annotation.Singleton;
import com.treilhes.jfxplace.app.debugtools.api.events.DebugEvents;
import com.treilhes.jfxplace.app.debugtools.api.ui.Docks;
import com.treilhes.jfxplace.app.debugtools.app.ui.view.ContextTreeController.ContextNode;
import com.treilhes.jfxplace.core.api.application.Application;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.ui.controller.AbstractFxmlViewController;
import com.treilhes.jfxplace.core.api.ui.controller.dock.annotation.ViewAttachment;
import com.treilhes.jfxplace.core.api.ui.controller.menu.ViewMenu;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

//@formatter:off
@ApplicationInstanceSingleton
@ViewAttachment(
        name = "Context Beans",
        id = "35ce9cfd-bcf6-4eda-b4d3-a580a186a5d4",
        prefDockId = Docks.BOTTOM_DOCK_ID,
        openOnStart = true,
        selectOnStart = false,
        order = 4000,
        icon = "openapi_tool.png",
        iconX2 = "openapi_tool@2x.png"
        )
//@formatter:on
public class ContextBeansController extends AbstractFxmlViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextBeansController.class);

    private static final Comparator<ContextNode> CONTEXT_NODE_COMPARATOR = Comparator.comparing(n -> n.type());

    @FXML
    private Label noContextLabel;

    @FXML
    private TableView<Class<?>> tableView;

    @FXML
    private TableColumn<Class<?>, String> classColumn;

    @FXML
    private TableColumn<Class<?>, String> moduleColumn;

    @FXML
    private TableColumn<Class<?>, String> scopeColumn;

    private final DebugEvents debugEvents;

    //@formatter:off
    protected ContextBeansController(
            Application application,
            ApplicationInstance instance,
            DebugEvents debugEvents,
            ViewMenu viewMenu) {
        //@formatter:on
        super(application.getI18n(), application.getEvents(), instance.getEvents(), viewMenu, ContextBeansController.class.getResource("ContextBeansView.fxml"));
        this.debugEvents = debugEvents;
    }

    @FXML
    public void initialize() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        classColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSimpleName()));
        moduleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getModule().getName()));
        scopeColumn.setCellValueFactory(cellData -> {
            var cls = cellData.getValue();
            for (var annotation:cls.getAnnotations()) {
                ReadOnlyStringWrapper value = switch(annotation) {
                    case Singleton a -> new ReadOnlyStringWrapper(a.annotationType().getSimpleName());
                    case ApplicationConfiguration a -> new ReadOnlyStringWrapper(a.annotationType().getSimpleName());
                    case ApplicationSingleton a -> new ReadOnlyStringWrapper(a.annotationType().getSimpleName());
                    case ApplicationPrototype a -> new ReadOnlyStringWrapper(a.annotationType().getSimpleName());
                    case ApplicationInstanceSingleton a -> new ReadOnlyStringWrapper(a.annotationType().getSimpleName());
                    case ApplicationInstancePrototype a -> new ReadOnlyStringWrapper(a.annotationType().getSimpleName());
                    case DeportedSingleton a -> new ReadOnlyStringWrapper(a.annotationType().getSimpleName());
                    case Scope a -> new ReadOnlyStringWrapper(a.scopeName());
                    case Component a -> new ReadOnlyStringWrapper(a.annotationType().getSimpleName());
                    default -> null;
                };
                if(value != null) {
                    return value;
                }
            }
            return new ReadOnlyStringWrapper("");
        });
        debugEvents.context().subscribe(c -> updateTableView());

        updateTableView();
    }


    @Override
    public void controllerDidLoadFxml() {
        getRoot().setId(ContextBeansController.class.getSimpleName());
        getRoot().minWidth(400.0);
        getRoot().minHeight(400.0);
    }

    @Override
    public void onShow() {
        updateTableView();
    }

    @Override
    public void onHidden() {
        clearTableView();
    }

    protected void clearTableView() {
        tableView.getItems().clear();
    }

    protected void updateTableView() {
        clearTableView();

        var context = debugEvents.context().get();

        var hasContext = context != null;

        noContextLabel.setVisible(!hasContext);
        tableView.setVisible(hasContext);

        if(!hasContext) {
            return;
        }

        tableView.getItems().addAll(context.getRegisteredClasses());
    }


    protected void destroy() {
        clearTableView();
    }

}
