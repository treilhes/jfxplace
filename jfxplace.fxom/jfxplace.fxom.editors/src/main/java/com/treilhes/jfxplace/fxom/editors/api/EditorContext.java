package com.treilhes.jfxplace.fxom.editors.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.treilhes.jfxplace.core.api.fs.FileSystem;
import com.treilhes.jfxplace.core.api.glossary.Glossary;
import com.treilhes.jfxplace.core.api.i18n.I18N;
import com.treilhes.jfxplace.core.api.ui.controller.misc.MessageLogger;
import com.treilhes.jfxplace.core.api.ui.dialog.Dialog;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.FXOMElement;
import com.treilhes.jfxplace.core.metadata.AbstractMetadata;
import com.treilhes.jfxplace.core.metadata.BasicSelection;
import com.treilhes.jfxplace.core.metadata.property.ValuePropertyMetadata;

import javafx.scene.effect.Effect;

public interface EditorContext extends BasicSelection {

    I18N i18n();
    Dialog dialog();
    FileSystem fileSystem();

    void openDocumentationUrl(ValuePropertyMetadata propMeta);

    FXOMDocument getDocument();

    Set<Class<?>> getSelectedClasses();

    @Override
    Set<FXOMElement> getSelectedInstances();

    Map<String, String> getStyleClassesMap();

    List<Class<? extends Effect>> listEffects();

    AbstractMetadata metadata();

    MessageLogger messageLogger();

    Glossary glossary();
    List<String> getThemeClasses();
    List<String> getCssProperties();
}
