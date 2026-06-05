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

import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import com.treilhes.emc4j.boot.api.context.ContextManager;
import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.emc4j.boot.api.context.beans.ExtensionDefinition;
import com.treilhes.emc4j.boot.api.layer.Layer;
import com.treilhes.emc4j.boot.api.layer.ModuleLayerManager;
import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.treilhes.emc4j.boot.api.loader.extension.SealedExtension;
import com.treilhes.jfxplace.app.debugtools.api.events.DebugEvents;
import com.treilhes.jfxplace.app.debugtools.api.ui.Docks;
import com.treilhes.jfxplace.core.api.application.Application;
import com.treilhes.jfxplace.core.api.application.InstancesManager;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.ui.controller.AbstractFxmlViewController;
import com.treilhes.jfxplace.core.api.ui.controller.dock.annotation.ViewAttachment;
import com.treilhes.jfxplace.core.api.ui.controller.menu.ViewMenu;

import jakarta.annotation.PreDestroy;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

//@formatter:off
@ApplicationInstanceSingleton
@ViewAttachment(
        name = "Context Tree",
        id = "9d9978de-a803-4914-8eb1-0b4371d9e6ed",
        prefDockId = Docks.LEFT_DOCK_ID,
        openOnStart = true,
        selectOnStart = false,
        order = 4000,
        icon = "openapi_tool.png",
        iconX2 = "openapi_tool@2x.png"
        )
//@formatter:on
public class ContextTreeController extends AbstractFxmlViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextTreeController.class);

    private static final Comparator<ContextNode> CONTEXT_NODE_COMPARATOR = Comparator.comparing(n -> n.type());

    private static final int REFRESH_RATE_SECONDS = 10;

    @FXML
    TreeView<ContextNode> treeView;

    private final ApplicationInstance instance;
    private final ModuleLayerManager moduleLayerManager;
    private final ContextManager contextManager;
    private final DebugEvents debugEvents;
    private final ScheduledExecutorService scheduler;

    private ChangeListener<? super TreeItem<ContextNode>> listener;
    private ScheduledFuture<?> scheduledTask;

    //@formatter:off
    protected ContextTreeController(
            Application application,
            ApplicationInstance instance,
            DebugEvents debugEvents,
            ViewMenu viewMenu,
            ModuleLayerManager moduleLayerManager,
            ContextManager contextManager) {
        //@formatter:on
        super(application.getI18n(), application.getEvents(), instance.getEvents(), viewMenu, ContextTreeController.class.getResource("ContextView.fxml"));
        this.instance = instance;
        this.debugEvents = debugEvents;
        this.moduleLayerManager = moduleLayerManager;
        this.contextManager = contextManager;
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @FXML
    public void initialize() {
        listener = (obj, o, n) -> {
            if (n != null && debugEvents.context().get() != n.getValue().context()) {
                debugEvents.context().set(n.getValue().context());
            }
        };
        treeView.getSelectionModel().selectedItemProperty().addListener(listener);

        treeView.setCellFactory(t -> {
            TreeCell<ContextNode> cell = new TreeCell<ContextNode>() {
                @Override
                public void updateItem(ContextNode item, boolean empty) {
                    super.updateItem(item, empty);
                    // check if the cell is empty (no data) or if the data is null
                    if (item == null || empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        var layer = item.layer();
                        var context = item.context();
                        var extension = item.extension();

                        setText(item.type() + ":" + context.getId());
                    }
                }
            };
            return cell;
        });
        scheduleTreeUpdate();
    }

    private void scheduleTreeUpdate() {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = scheduler.scheduleAtFixedRate(this::updateTreeView, 0, REFRESH_RATE_SECONDS,
                    TimeUnit.SECONDS);
        }
    }

    private void cancelScheduledTreeUpdate() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            scheduledTask = null;
        }
    }

    @Override
    public void controllerDidLoadFxml() {
        getRoot().setId(ContextTreeController.class.getSimpleName());
        getRoot().minWidth(400.0);
        getRoot().minHeight(400.0);
    }

    @Override
    public void onShow() {
        scheduleTreeUpdate();
    }

    @Override
    public void onHidden() {
        cancelScheduledTreeUpdate();
        clearTreeView();
    }

    protected void clearTreeView() {
        instance.getExecutor().runOnFxThread(this::doClearTreeView);
    }

    private void doClearTreeView() {
        treeView.setRoot(null);
    }

    protected void updateTreeView() {
        instance.getExecutor().runOnFxThread(this::doUpdateTreeView);
    }

    private void doUpdateTreeView() {

        treeView.getSelectionModel().selectedItemProperty().removeListener(listener);

        var selectedItem = treeView.getSelectionModel().getSelectedItem();
        //var selectedRow = treeView.getRow(selectedItem);
        var selectedRow = treeView.getSelectionModel().getSelectedIndex();
        var focusedItem = treeView.getFocusModel().getFocusedItem();
        //var focusedRow = treeView.getRow(focusedItem);
        var focusedRow = treeView.getFocusModel().getFocusedIndex();


        if (treeView.getRoot() == null) {
            var bootContext = contextManager.getBootContext();
            var bootItem = createTreeNode(ContextNodeType.BOOT ,null, bootContext, null);
            bootItem.setExpanded(true);
            treeView.setRoot(bootItem);
        }

        var bootItem = treeView.getRoot();
        if (bootItem.getChildren().isEmpty()) {
            loadRooItem(Extension.ROOT_ID).ifPresent(bootItem.getChildren()::add);
        }

        if (!bootItem.getChildren().isEmpty()) {
            var rootItem = bootItem.getChildren().get(0);
            syncChildren(rootItem);
        }

        sortTree(treeView.getRoot(), CONTEXT_NODE_COMPARATOR);
        treeView.getSelectionModel().selectedItemProperty().addListener(listener);

        if (selectedItem != null && !selectedItem.equals(treeView.getSelectionModel().getSelectedItem())) {
            treeView.getSelectionModel().select(selectedRow);
        }
        treeView.getFocusModel().focus(focusedRow);
    }


    /**
     * Sync the children of a tree item with the children layers of a given layer id
     * - first clear the items if the layer can't be found anymore and return fast
     * - then identify candidate layers
     * - then remove tree items that are not in the candidate list
     * - then filter the tree items in the candidate list that are already in the treeview level
     * - finaly add the remaining candidates
     * @param id the layer id to sync with
     * @param children the tree items to sync
     * @param filter the filter to apply on the extensions of the layers
     */
    private void syncChildren(TreeItem<ContextNode> node) {

        List<TreeItem<ContextNode>> children = node.getChildren();
        var ctx = node.getValue().context();

        if (ctx == null) {
            children.clear();
            return;
        }

        var id = node.getValue().context().getUuid();
        var layer = moduleLayerManager.get(id);

        if (layer == null) {
            children.clear();
            return;
        }

        var childLayers = layer.getChildren();

        var treeItemCandidates = childLayers.stream()
                //convert all layers to a treeitem candidate
                .map(l -> {
                    // ensure the layer referenced, has a context and an extension
                    var layerFromMap = moduleLayerManager.get(l.getId());
                    var context = contextManager.get(l.getId());

                    if (layerFromMap != null && context != null) {
                        var extensionDef = context.getLocalBean(ExtensionDefinition.class);
                        if (extensionDef == null) {
                            return null;
                        }
                        var extension = extensionDef.getExtension();
                        var type = extension instanceof SealedExtension ? ContextNodeType.APPLICATION
                                : ContextNodeType.EXTENSION;
                        return createTreeNode(type, layerFromMap, context, extension);
                    }
                    return null;
                })
                //filter out non matching layers
                .filter(Objects::nonNull)
                .toList();

        // remove items that are not in the candidates list
        children.removeIf(tn -> {
            var childLayer = tn.getValue().layer();

            if (childLayer == null) { // layer deleted, remove item
                return true;
            }

            return treeItemCandidates.stream()
                    .noneMatch(c -> c.getValue().layer() == childLayer);
        });

        // remove items already existing in the treeview level
        // and add logic nodes for the remaining candidates
        var treeItemToAdd = treeItemCandidates.stream()
                //filter out already existing items
                .filter(cl -> children.stream()
                        .noneMatch(i -> i.getValue().layer() == cl.getValue().layer()))
                .toList();

         children.addAll(treeItemToAdd);

         // if the extension is an application extension, then instances has been removed, add them back
         try {
            var layerNode = node.getValue();
             var extension = layerNode.extension();
             var context = layerNode.context();
             if (context != null && extension != null && extension instanceof SealedExtension) {
                 var appManager = context.getBean(InstancesManager.class);
                 var instanceNodes = appManager.getInstances().stream()
                         .map(i -> createTreeNode(ContextNodeType.INSTANCE, null, i.getContext(), null))
                         .toList();

                 children.addAll(instanceNodes);
             }
        } catch (BeansException e) {
            LOGGER.error("Error while loading application instances for layer " + node.getValue().layer(), e);
        }


         children.forEach(this::syncChildren);
    }

    private Optional<TreeItem<ContextNode>> loadRooItem(UUID layerId) {
        var layer = moduleLayerManager.get(layerId);
        var context = contextManager.get(layerId);

        if (context == null) {
            return Optional.empty();
        }

        var extension = context.getLocalBean(Extension.class);
        var item = createTreeNode(ContextNodeType.ROOT, layer, context, extension);

        return Optional.of(item);
    }

    TreeItem<ContextNode> createTreeNode(ContextNodeType type, @Nullable Layer layer, @NonNull EmContext context, @Nullable Extension extension) {
        var node = new ContextNode(type, layer, context, extension);
        var item =  new TreeItem<>(node);
        item.setExpanded(true);
        return item;
    }

    @PreDestroy
    protected void destroy() {
        cancelScheduledTreeUpdate();
        clearTreeView();
        scheduler.shutdownNow();
    }

    public static <T> void sortTree(
            TreeItem<T> item,
            Comparator<? super T> comparator) {

        item.getChildren().sort(
                Comparator.comparing(TreeItem::getValue, comparator));

        for (TreeItem<T> child : item.getChildren()) {
            sortTree(child, comparator);
        }
    }

    /**
     * A node in the context tree, it can represent the boot context, an application, an instance or an extension
     * Declaration order of the fields is important as they are used in the comparator to sort the tree items
     */
    enum ContextNodeType {
        BOOT,
        ROOT,
        APPLICATION,
        INSTANCE,
        EXTENSION
    }
    static class ContextNode{
        private ContextNodeType type;
        private WeakReference<Layer> layer;
        private WeakReference<EmContext> context;
        private WeakReference<Extension> extension;

        public ContextNode(ContextNodeType type, Layer layer, EmContext context, Extension extension) {
            this.type = type;
            this.layer = new WeakReference<>(layer);
            this.context = new WeakReference<>(context);
            this.extension = new WeakReference<>(extension);
        }

        public ContextNodeType type() {
            return type;
        }
        public Layer layer() {
            return layer.get();
        }
        public EmContext context() {
            return context.get();
        }
        public Extension extension() {
            return extension.get();
        }

        @Override
        public int hashCode() {
            return Objects.hash(context.get(), extension.get(), layer.get(), type);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ContextNode other = (ContextNode) obj;
            var eq = Objects.equals(context.get(), other.context.get()) && Objects.equals(extension.get(), other.extension.get())
                    && Objects.equals(layer.get(), other.layer.get()) && type == other.type;

            System.out.println("Comparing " + this.context.get() + " with " + other.context.get() + " : " + eq);
            return eq;
        }


    }
}
