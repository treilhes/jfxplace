package com.treilhes.jfxplace.core.fxom.pipeline;

import java.util.Map;

import com.treilhes.jfxplace.core.fxom.FXOMDocument;

public interface FXOMPostDeserialization {
    FXOMDocument postDeserializationTransform(FXOMDocument document, Map<Class<?>, Object> processContext);
}
