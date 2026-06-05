package com.treilhes.jfxplace.app.debugtools.app.ui.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

import com.treilhes.emc4j.boot.api.context.ContextManager;
import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.beans.ExtensionDefinition;
import com.treilhes.emc4j.boot.api.layer.Layer;
import com.treilhes.emc4j.boot.api.layer.ModuleLayerManager;
import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.treilhes.emc4j.boot.api.loader.extension.SealedExtension;
import com.treilhes.emc4j.test.EmcInject;
import com.treilhes.emc4j.test.EmcInjectMock;
import com.treilhes.emc4j.test.EmcInjectSpy;
import com.treilhes.jfxplace.app.debugtools.api.events.DebugEvents;
import com.treilhes.jfxplace.app.debugtools.app.ui.view.ContextTreeController.ContextNode;
import com.treilhes.jfxplace.core.api.application.InstancesManager;
import com.treilhes.jfxplace.core.api.ui.controller.menu.ViewMenu;
import com.treilhes.jfxplace.test.JfxPlaceTest;
import com.treilhes.jfxplace.test.builder.StageBuilder;
import com.treilhes.jfxplace.test.builder.StageType;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

@JfxPlaceTest(classes = ContextTreeController.class)
class ContextTreeControllerTest {

    @EmcInject(create = true)
    DebugEvents.DebugEventsImpl debugEvents;

    @EmcInjectMock
    ViewMenu viewMenu;

    @EmcInjectMock
    ModuleLayerManager moduleLayerManager;

    @EmcInjectSpy
    ContextManager contextManager;

    @EmcInject
    StageBuilder builder;

    @Test
    void controller_must_load_successfully(FxRobot robot) {
        try (var testStage = builder.controller(ContextTreeController.class)
                .setup(StageType.Fill)
                .size(800, 600)
                .show()){
            assertNotNull(testStage.getController());
        }
    }

    @Test
    void treeview_must_be_initialized_with_the_boot_context(FxRobot robot) {
        try (var testStage = builder.controller(ContextTreeController.class)
                .setup(StageType.Fill)
                .size(800, 600)
                .show()){

            var treeview = robot.lookup(("#context-view-treeview")).queryAs(TreeView.class);

            assertNotNull(testStage.getController());
            assertNotNull(treeview);
            assertNotNull(treeview.getRoot());
            assertEquals(0, treeview.getRoot().getChildren().size());
        }
    }

    @Test
    void treeview_must_contains_the_root_context_on_show(FxRobot robot) {
        var rootLayer = mock(Layer.class);
        var rootContext = mock(EmContext.class);
        when(rootContext.getId()).thenReturn(Extension.ROOT_ID.toString());
        when(contextManager.get(Extension.ROOT_ID)).thenReturn(rootContext);
        when(moduleLayerManager.get(Extension.ROOT_ID)).thenReturn(rootLayer);

        try (var testStage = builder.controller(ContextTreeController.class)
                .setup(StageType.Fill)
                .size(800, 600)
                .show()){

            var controller = testStage.getController();
            var treeview = robot.lookup(("#context-view-treeview")).queryAs(TreeView.class);

            controller.onShow();

            assertNotNull(testStage.getController());
            assertNotNull(treeview);
            assertNotNull(treeview.getRoot());
            assertEquals(1, treeview.getRoot().getChildren().size());
        }
    }

    @Test
    void treeview_must_load_extensions_in_root_node(FxRobot robot) {

        var rootLayer = mock(Layer.class);
        var rootContext = mock(EmContext.class);

        when(rootContext.getId()).thenReturn(Extension.ROOT_ID.toString());
        when(rootContext.getUuid()).thenReturn(Extension.ROOT_ID);
        when(contextManager.get(Extension.ROOT_ID)).thenReturn(rootContext);
        when(moduleLayerManager.get(Extension.ROOT_ID)).thenReturn(rootLayer);

        var appId = UUID.randomUUID();
        var appLayer = mock(Layer.class);
        var appContext = mock(EmContext.class);
        var appExtension = mock(SealedExtension.class); // simulate an application extension
        var appExtDef = mock(ExtensionDefinition.class);
        var appInstancesManager = mock(InstancesManager.class);

        when(rootLayer.getChildren()).thenReturn(Set.of(appLayer));
        when(appLayer.getId()).thenReturn(appId);
        when(appContext.getUuid()).thenReturn(appId);
        when(appContext.getId()).thenReturn(appId.toString());
        when(appContext.getBean(InstancesManager.class)).thenReturn(appInstancesManager);
        when(appInstancesManager.getInstances()).thenReturn(List.of());
        when(appExtDef.getExtension()).thenReturn(appExtension);

        doReturn(appExtDef).when(appContext).getLocalBean(argThat(ExtensionDefinition.class::isAssignableFrom));

        when(contextManager.get(appId)).thenReturn(appContext);
        when(moduleLayerManager.get(appId)).thenReturn(appLayer);

        try (var testStage = builder.controller(ContextTreeController.class)
                .setup(StageType.Fill)
                .size(800, 600)
                .show()){

            var controller = testStage.getController();
            var treeview = robot.lookup(("#context-view-treeview")).queryAs(TreeView.class);

            controller.onShow();

            assertNotNull(testStage.getController());
            assertNotNull(treeview);
            assertNotNull(treeview.getRoot());

            TreeItem<ContextNode> bootItem = treeview.getRoot();
            TreeItem<ContextNode> rootItem = bootItem.getChildren().get(0);
            // ensure the application extension is stored in the applications logic node
            assertEquals(1, rootItem.getChildren().size());
        }
    }

    @Test
    void treeview_selection_update_debugevents_current_context(FxRobot robot) {
        var rootLayer = mock(Layer.class);
        var rootContext = mock(EmContext.class);
        when(rootContext.getId()).thenReturn(Extension.ROOT_ID.toString());
        when(contextManager.get(Extension.ROOT_ID)).thenReturn(rootContext);
        when(moduleLayerManager.get(Extension.ROOT_ID)).thenReturn(rootLayer);

        try (var testStage = builder.controller(ContextTreeController.class)
                .setup(StageType.Fill)
                .size(800, 600)
                .show()){

            var controller = testStage.getController();
            var treeview = robot.lookup(("#context-view-treeview")).queryAs(TreeView.class);

            controller.onShow();

            assertNotNull(testStage.getController());
            assertNotNull(treeview);
            assertNotNull(treeview.getRoot());
            assertEquals(1, treeview.getRoot().getChildren().size());

            TreeItem<ContextNode> boot = treeview.getRoot();
            var root = boot.getChildren().get(0);

            // select the root context node
            robot.interact(() -> {
                treeview.getSelectionModel().select(root);
            });

            // ensure the debug events current context is updated with the selected context
            assertEquals(root.getValue().context(), debugEvents.context().get());

            // select the boot context node
            robot.interact(() -> {
                treeview.getSelectionModel().select(boot);
            });

            // ensure the debug events current context is updated with the selected context
            assertEquals(boot.getValue().context(), debugEvents.context().get());
        }
    }
}
