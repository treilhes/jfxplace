package com.treilhes.jfxplace.core.fxom.pipeline;

import java.util.Map;

public interface FXOMPostSerialization {
    String postSerializationTransform(String fxmlString, Map<Class<?>, Object> processContext);
}
