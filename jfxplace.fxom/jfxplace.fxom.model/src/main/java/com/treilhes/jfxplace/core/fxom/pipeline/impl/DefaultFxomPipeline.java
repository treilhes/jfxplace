package com.treilhes.jfxplace.core.fxom.pipeline.impl;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.jspecify.annotations.NonNull;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMDocumentFactory;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPipeline;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPostDeserialization;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPostSerialization;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPreDeserialization;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPreSerialization;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMSerializer;

@ApplicationInstanceSingleton
public class DefaultFxomPipeline implements FXOMPipeline {

    public static final DefaultFxomPipeline BASIC = new DefaultFxomPipeline(
            List.of(),
            List.of(),
            List.of(new DefaultJavaFxNamespacePreSerializationStep(), new UpdateImportsPreSerializationStep()),
            List.of(),
            FXOMDocumentFactory.DEFAULT,
            DefaultFxmlSerializer.DEFAULT_FXML
    );

    private final List<FXOMPreDeserialization> preDeserializations;
    private final List<FXOMPostDeserialization> postDeserializations;
    private final List<FXOMPreSerialization> preSerializations;
    private final List<FXOMPostSerialization> postSerializations;
    private final FXOMDocumentFactory fxomDocumentFactory;
    private final FXOMSerializer fxomSerializer;

    public DefaultFxomPipeline(
            List<FXOMPreDeserialization> preDeserializations,
            List<FXOMPostDeserialization> postDeserializations,
            List<FXOMPreSerialization> preSerializations,
            List<FXOMPostSerialization> postSerializations,
            FXOMDocumentFactory fxomDocumentFactory,
            FXOMSerializer fxomSerializer) {
        this.preDeserializations = preDeserializations;
        this.postDeserializations = postDeserializations;
        this.preSerializations = preSerializations;
        this.postSerializations = postSerializations;
        this.fxomDocumentFactory = fxomDocumentFactory;
        this.fxomSerializer = fxomSerializer;
    }

    @Override
    public FXOMDocument deserialize(String fxomString) throws IOException {
        return deserialize(fxomString, new HashMap<>());
    }

    private FXOMDocument deserialize(String fxomString, @NonNull HashMap<Class<?>, Object> processContext)
            throws IOException {

        for (FXOMPreDeserialization preDeserialization : preDeserializations) {
            fxomString = preDeserialization.preDeserializationTransform(fxomString, processContext);
        }

        var document = fxomDocumentFactory.newDocument(fxomString);

        for (FXOMPostDeserialization postDeserialization : postDeserializations) {
            document = postDeserialization.postDeserializationTransform(document, processContext);
        }

        return document;
    }

    @Override
    public FXOMDocument deserialize(String fxomString, URL location, ClassLoader classLoader, ResourceBundle resources, boolean normalize) throws IOException {
        return deserialize(fxomString, location, classLoader, resources, normalize, new HashMap<>());
    }

    private FXOMDocument deserialize(String fxomString, URL location, ClassLoader classLoader, ResourceBundle resources,
            boolean normalize, @NonNull HashMap<Class<?>, Object> processContext) throws IOException {

        for (FXOMPreDeserialization preDeserialization : preDeserializations) {
            fxomString = preDeserialization.preDeserializationTransform(fxomString, processContext);
        }

        var document = fxomDocumentFactory.newDocument(fxomString, location, classLoader, resources, normalize);

        for (FXOMPostDeserialization postDeserialization : postDeserializations) {
            document = postDeserialization.postDeserializationTransform(document, processContext);
        }

        return document;
    }

    @Override
    public String serialize(FXOMDocument fxomDocument) {
        return serialize(fxomDocument, new HashMap<>());
    }

    private String serialize(FXOMDocument fxomDocument, @NonNull HashMap<Class<?>, Object> processContext) {

        for (FXOMPreSerialization preSerialization : preSerializations) {
            fxomDocument = preSerialization.preSerializationTransform(fxomDocument, processContext);
        }

        String fxomString = fxomSerializer.serialize(fxomDocument);

        for (FXOMPostSerialization postSerialization : postSerializations) {
            fxomString = postSerialization.postSerializationTransform(fxomString, processContext);
        }

        return fxomString;
    }

    @Override
    public FXOMDocument clone(FXOMDocument fxomDocument) throws IOException {
        var processContext = new HashMap<Class<?>, Object>();
        var fxomString = serialize(fxomDocument, processContext);

        var url = fxomDocument.getLocation();
        var classLoader = fxomDocument.getClassLoader();
        var resources = fxomDocument.getResources();
        var normalize = fxomDocument.isNormalized();

        return deserialize(fxomString, url, classLoader, resources, normalize, processContext);
    }

}
