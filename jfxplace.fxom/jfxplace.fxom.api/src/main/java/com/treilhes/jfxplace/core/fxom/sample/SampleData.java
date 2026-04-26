package com.treilhes.jfxplace.core.fxom.sample;

import com.treilhes.jfxplace.core.fxom.FXOMDocument;

public interface SampleData {
    void populate(FXOMDocument fxomDocument);
    void cleanup(FXOMDocument fxomDocument);
}
