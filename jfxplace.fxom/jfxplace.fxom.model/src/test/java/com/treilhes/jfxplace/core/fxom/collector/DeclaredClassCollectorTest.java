package com.treilhes.jfxplace.core.fxom.collector;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import com.treilhes.jfxplace.core.fxom.pipeline.FXOMDocumentFactory;

@ExtendWith(ApplicationExtension.class)
//@SetSystemProperty(key = "javafx.allowjs", value = "true")
class DeclaredClassCollectorTest {

    @Test
    void must_collect_element_and_property_class() throws Exception {
        var fxml = """
                <?xml version="1.0" encoding="UTF-8"?>

                <?import javafx.scene.control.*?>
                <?import javafx.scene.layout.*?>

                <ToolBar  VBox.vgrow="SOMETIMES" prefHeight="40.0" prefWidth="318.0" xmlns="http://javafx.com/javafx/8.0.65" xmlns:fx="http://javafx.com/fxml/1">
                   <Button text="Apples" />
                   <Button text="Oranges" HBox.hgrow="NEVER" />
                   <Pane HBox.hgrow="ALWAYS"/>
                   <Button text="Help" />
                </ToolBar>
                """;

        var fxomDocument = FXOMDocumentFactory.DEFAULT.newDocument(fxml);
        var result = fxomDocument.collect(DeclaredClassCollector.all());

        // from elements
        assertTrue(result.contains(javafx.scene.control.ToolBar.class));
        assertTrue(result.contains(javafx.scene.control.Button.class));
        assertTrue(result.contains(javafx.scene.layout.Pane.class));

        // from properties
        assertTrue(result.contains(javafx.scene.layout.VBox.class));
        assertTrue(result.contains(javafx.scene.layout.HBox.class));

    }

}
