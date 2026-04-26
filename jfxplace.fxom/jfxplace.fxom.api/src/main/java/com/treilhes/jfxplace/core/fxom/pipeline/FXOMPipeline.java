package com.treilhes.jfxplace.core.fxom.pipeline;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.treilhes.jfxplace.core.fxom.FXOMDocument;

/**
 * FXOMPipeline defines the contract for serializing and deserializing FXOMDocument instances.
 * Implementations of this interface can provide different strategies for handling FXOM data, such as
 * custom serialization formats, optimizations, or support for additional features.
 * <br>
 * It is expected that the deserialize method will take a string representation of an FXOM document and return a corresponding FXOMDocument object,
 * while the serialize method will convert an FXOMDocument back into its string representation. The clone method allows for creating a deep copy of an FXOMDocument.
 * Implementations should ensure that the clone method produces a new instance of FXOMDocument that is identical in content but independent of the original.
 */
public interface FXOMPipeline {
    /**
     * Deserializes a string representation of an FXOM document into an FXOMDocument object:<br>
     * <br>
     * The expected implementation should provide the following minimal pipeline:<br>
     * String -> {@link FXOMPreDeserialization}* -> {@link FXOMDocumentFactory} -> {@link FXOMPostDeserialization}* -> FXOMDocument
     *
     * @param fxomString the string representation of the FXOM document to deserialize
     * @return the deserialized FXOMDocument object
     * @throws IOException
     */
    FXOMDocument deserialize(String fxomString) throws IOException;

    FXOMDocument deserialize(String fxomString, URL location, ClassLoader classLoader, ResourceBundle resources,
            boolean normalize) throws IOException;

    /**
     * Serializes an FXOMDocument object into its string representation:<br>
     * <br>
     * The expected implementation should provide the following minimal pipeline:<br>
     * FXOMDocument -> {@link FXOMPreSerialization}* -> {@link FXOMSerializer} -> {@link FXOMPostSerialization}* -> String
     *
     * @param fxomDocument the FXOMDocument to serialize
     * @return the string representation of the FXOMDocument
     */
    String serialize(FXOMDocument fxomDocument);
    /**
     * Creates a deep copy of the given FXOMDocument by chaining the following minimal pipeline:<br>
     * FXOMDocument -> {@link FXOMPipeline#serialize(FXOMDocument)} -> {@link FXOMPipeline#deserialize(String)} -> FXOMDocument
     *
     * @param fxomDocument the FXOMDocument to clone
     * @return a new FXOMDocument that is a deep copy of the original
     * @throws IOException
     */
    FXOMDocument clone(FXOMDocument fxomDocument) throws IOException;

}
