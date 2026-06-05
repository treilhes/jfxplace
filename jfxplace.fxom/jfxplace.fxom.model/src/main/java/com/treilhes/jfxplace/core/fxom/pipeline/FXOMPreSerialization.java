package com.treilhes.jfxplace.core.fxom.pipeline;

import java.util.Map;

import com.treilhes.jfxplace.core.fxom.FXOMDocument;

public interface FXOMPreSerialization {
    FXOMDocument preSerializationTransform(FXOMDocument document, Map<Class<?>, Object> processContext);
}
