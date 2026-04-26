package com.treilhes.jfxplace.core.fxom.pipeline;

import com.treilhes.jfxplace.core.fxom.FXOMDocument;

public interface FXOMPostDeserialization {
    FXOMDocument postDeserializationTransform(FXOMDocument document);
}
