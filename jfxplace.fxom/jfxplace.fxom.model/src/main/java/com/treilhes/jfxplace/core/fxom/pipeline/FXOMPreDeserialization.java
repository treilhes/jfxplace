package com.treilhes.jfxplace.core.fxom.pipeline;

import java.util.Map;

public interface FXOMPreDeserialization {
    String preDeserializationTransform(String document, Map<Class<?>, Object> processContext);
}
