package com.treilhes.jfxplace.app.debugtools.app.ui.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

import com.treilhes.emc4j.test.EmcInject;
import com.treilhes.emc4j.test.EmcInjectMock;
import com.treilhes.jfxplace.app.debugtools.api.events.DebugEvents;
import com.treilhes.jfxplace.core.api.ui.controller.menu.ViewMenu;
import com.treilhes.jfxplace.test.JfxPlaceTest;
import com.treilhes.jfxplace.test.builder.StageBuilder;
import com.treilhes.jfxplace.test.builder.StageType;

import javafx.scene.control.TableView;

@JfxPlaceTest(classes = ContextBeansController.class)
class ContextBeansControllerTest {

    @EmcInject(create = true)
    DebugEvents.DebugEventsImpl debugEvents;

    @EmcInjectMock
    ViewMenu viewMenu;

    @EmcInject
    StageBuilder builder;

    @Test
    void controller_must_load_successfully(FxRobot robot) {
        try (var testStage = builder.controller(ContextBeansController.class)
                .setup(StageType.Fill)
                .size(800, 600)
                .show()){
            assertNotNull(testStage.getController());
        }
    }

    @Test
    void tableview_must_be_initialized(FxRobot robot) {
        try (var testStage = builder.controller(ContextBeansController.class)
                .setup(StageType.Fill)
                .size(800, 600)
                .show()){

            var tableView = robot.lookup(("#context-beans-tableview")).queryAs(TableView.class);

            assertNotNull(testStage.getController());
            assertNotNull(tableView);
        }
    }
}
