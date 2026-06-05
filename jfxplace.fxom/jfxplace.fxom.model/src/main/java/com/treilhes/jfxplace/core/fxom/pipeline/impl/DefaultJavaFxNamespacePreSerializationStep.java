package com.treilhes.jfxplace.core.fxom.pipeline.impl;

import java.util.Map;

import org.jspecify.annotations.NonNull;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.FXOMObject;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPreSerialization;

@ApplicationSingleton
public class DefaultJavaFxNamespacePreSerializationStep implements FXOMPreSerialization {

    private static final String JAVAFX_VERSION = System.getProperty("javafx.version");
    private static final String NAME_SPACE_FX_FORMAT = "http://javafx.com/javafx/%s"; // NOI18N
    private static final String NAME_SPACE_FXML = "http://javafx.com/fxml/1"; // NOI18N

    @Override
    public FXOMDocument preSerializationTransform(FXOMDocument document, @NonNull Map<Class<?>, Object> processContext) {
        if (document.getFxomRoot() != null) {
            updateNameSpace(document, System.getProperty("javafx.version"));
        }
        return document;
    }

    private void updateNameSpace(FXOMDocument fxomDocument, String javafxVersion) {
        assert fxomDocument.getFxomRoot() != null;

        final FXOMObject fxomRoot = fxomDocument.getFxomRoot();
        final String currentNameSpaceFX = fxomRoot.getNameSpaceFX();
        final String currentNameSpaceFXML = fxomRoot.getNameSpaceFXML();

        String nameSpaceFx = String.format(NAME_SPACE_FX_FORMAT, javafxVersion);
        if ((currentNameSpaceFX == null) || (!currentNameSpaceFX.equals(nameSpaceFx))) {
            fxomRoot.setNameSpaceFX(nameSpaceFx);
        }

        if ((currentNameSpaceFXML == null) || (!currentNameSpaceFXML.equals(NAME_SPACE_FXML))) {
            fxomRoot.setNameSpaceFXML(NAME_SPACE_FXML);
        }

    }
}
