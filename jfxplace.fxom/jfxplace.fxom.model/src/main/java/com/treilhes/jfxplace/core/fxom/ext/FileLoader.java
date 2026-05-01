package com.treilhes.jfxplace.core.fxom.ext;

import java.io.File;
import java.io.IOException;

import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.FXOMObject;

public interface FileLoader {
    boolean canLoad(File file);
    FXOMObject loadInto(FXOMDocument targetDocument, File file) throws IOException;
}
