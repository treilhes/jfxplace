package com.treilhes.jfxplace.core.document.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import com.treilhes.jfxplace.core.document.preference.WildcardImportsPreference;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMDocumentFactory;

import javafx.stage.Stage;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class WildcardImportsPreSerializationStepTest {

    @Mock
    WildcardImportsPreference wildcardImportsPreference;

    @Start
    void start(Stage stage) {
        // nothing to do
    }

    @Test
    void must_keep_wildcard_when_provided_and_active() throws IOException {
        Mockito.when(wildcardImportsPreference.getValue()).thenReturn(true);// wilcard on
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

        var preSerializationStep = new WildcardImportsPreSerializationStep(wildcardImportsPreference);
        preSerializationStep.preSerializationTransform(fxomDocument, Map.of());

        ArrayList<String> imports = new ArrayList<>();
        fxomDocument.getGlue().collectInstructions("import").forEach(i -> imports.add(i.getData()));

        assertEquals("imports length should be 2", 2, imports.size());
        assertTrue(imports.contains("javafx.scene.control.*"));
        assertTrue(imports.contains("javafx.scene.layout.*"));
    }

    @Test
    void must_remove_wildcard_imports_when_provided_and_inactive() throws IOException {
        Mockito.when(wildcardImportsPreference.getValue()).thenReturn(false);// wilcard off

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

        var preSerializationStep = new WildcardImportsPreSerializationStep(wildcardImportsPreference);
        preSerializationStep.preSerializationTransform(fxomDocument, Map.of());

        ArrayList<String> imports = new ArrayList<>();
        fxomDocument.getGlue().collectInstructions("import").forEach(i -> imports.add(i.getData()));

        assertEquals("imports length should be 5", 5, imports.size());
        assertTrue(imports.contains("javafx.scene.control.ToolBar"));
        assertTrue(imports.contains("javafx.scene.control.Button"));
        assertTrue(imports.contains("javafx.scene.layout.Pane"));
        assertTrue(imports.contains("javafx.scene.layout.HBox"));
        assertTrue(imports.contains("javafx.scene.layout.VBox"));

    }

    @Test
    void must_keep_non_wilcard_imports_when_provided_and_inactive() throws IOException {
        Mockito.when(wildcardImportsPreference.getValue()).thenReturn(false);// wilcard off
        var fxml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <?import javafx.scene.control.ToolBar?>
                <?import javafx.scene.control.Button?>
                <?import javafx.scene.layout.Pane?>
                <?import javafx.scene.layout.HBox?>
                <?import javafx.scene.layout.VBox?>

                <ToolBar  VBox.vgrow="SOMETIMES" prefHeight="40.0" prefWidth="318.0" xmlns="http://javafx.com/javafx/8.0.65" xmlns:fx="http://javafx.com/fxml/1">
                   <Button text="Apples" />
                   <Button text="Oranges" HBox.hgrow="NEVER" />
                   <Pane HBox.hgrow="ALWAYS"/>
                   <Button text="Help" />
                </ToolBar>
                """;
        var fxomDocument = FXOMDocumentFactory.DEFAULT.newDocument(fxml);

        var preSerializationStep = new WildcardImportsPreSerializationStep(wildcardImportsPreference);
        preSerializationStep.preSerializationTransform(fxomDocument, Map.of());

        ArrayList<String> imports = new ArrayList<>();
        fxomDocument.getGlue().collectInstructions("import").forEach(i -> imports.add(i.getData()));

        assertEquals("imports length should be 5", 5, imports.size());
        assertTrue(imports.contains("javafx.scene.control.ToolBar"));
        assertTrue(imports.contains("javafx.scene.control.Button"));
        assertTrue(imports.contains("javafx.scene.layout.Pane"));
        assertTrue(imports.contains("javafx.scene.layout.HBox"));
        assertTrue(imports.contains("javafx.scene.layout.VBox"));

    }

    @Test
    void must_remove_non_wilcard_imports_when_provided_and_active() throws IOException {
        Mockito.when(wildcardImportsPreference.getValue()).thenReturn(true);// wilcard on
        var fxml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <?import javafx.scene.control.ToolBar?>
                <?import javafx.scene.control.Button?>
                <?import javafx.scene.layout.Pane?>
                <?import javafx.scene.layout.HBox?>
                <?import javafx.scene.layout.VBox?>

                <ToolBar  VBox.vgrow="SOMETIMES" prefHeight="40.0" prefWidth="318.0" xmlns="http://javafx.com/javafx/8.0.65" xmlns:fx="http://javafx.com/fxml/1">
                   <Button text="Apples" />
                   <Button text="Oranges" HBox.hgrow="NEVER" />
                   <Pane HBox.hgrow="ALWAYS"/>
                   <Button text="Help" />
                </ToolBar>
                """;
        var fxomDocument = FXOMDocumentFactory.DEFAULT.newDocument(fxml);

        var preSerializationStep = new WildcardImportsPreSerializationStep(wildcardImportsPreference);
        preSerializationStep.preSerializationTransform(fxomDocument, Map.of());

        ArrayList<String> imports = new ArrayList<>();
        fxomDocument.getGlue().collectInstructions("import").forEach(i -> imports.add(i.getData()));

        assertEquals("imports length should be 2", 2, imports.size());
        assertTrue(imports.contains("javafx.scene.control.*"));
        assertTrue(imports.contains("javafx.scene.layout.*"));

    }

}
