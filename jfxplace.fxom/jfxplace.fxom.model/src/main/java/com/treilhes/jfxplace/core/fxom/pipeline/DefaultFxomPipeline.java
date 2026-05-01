package com.treilhes.jfxplace.core.fxom.pipeline;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;

@ApplicationSingleton
public class DefaultFxomPipeline implements FXOMPipeline {

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
        for (FXOMPreDeserialization preDeserialization : preDeserializations) {
            fxomString = preDeserialization.preDeserializationTransform(fxomString);
        }

        var document = fxomDocumentFactory.newDocument(fxomString);

        for (FXOMPostDeserialization postDeserialization : postDeserializations) {
            document = postDeserialization.postDeserializationTransform(document);
        }

        return document;
    }

    @Override
    public FXOMDocument deserialize(String fxomString, URL location, ClassLoader classLoader, ResourceBundle resources, boolean normalize) throws IOException {
        for (FXOMPreDeserialization preDeserialization : preDeserializations) {
            fxomString = preDeserialization.preDeserializationTransform(fxomString);
        }

        var document = fxomDocumentFactory.newDocument(fxomString, location, classLoader, resources, normalize);

        for (FXOMPostDeserialization postDeserialization : postDeserializations) {
            document = postDeserialization.postDeserializationTransform(document);
        }

        return document;
    }

    @Override
    public String serialize(FXOMDocument fxomDocument) {
        for (FXOMPreSerialization preSerialization : preSerializations) {
            fxomDocument = preSerialization.preSerializationTransform(fxomDocument);
        }

        String fxomString = fxomSerializer.serialize(fxomDocument);

        for (FXOMPostSerialization postSerialization : postSerializations) {
            fxomString = postSerialization.postSerializationTransform(fxomString);
        }

        return fxomString;
    }

    @Override
    public FXOMDocument clone(FXOMDocument fxomDocument) throws IOException {
        var fxomString = serialize(fxomDocument);

        var url = fxomDocument.getLocation();
        var classLoader = fxomDocument.getClassLoader();
        var resources = fxomDocument.getResources();
        var normalize = fxomDocument.isNormalized();

        return deserialize(fxomString, url, classLoader, resources, normalize);
    }

}
