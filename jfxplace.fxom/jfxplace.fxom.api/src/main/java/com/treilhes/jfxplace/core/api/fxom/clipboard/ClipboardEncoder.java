package com.treilhes.jfxplace.core.api.fxom.clipboard;

import java.util.List;

import com.treilhes.jfxplace.core.fxom.FXOMObject;

import javafx.scene.input.ClipboardContent;

public interface ClipboardEncoder {

    boolean isEncodable(List<? extends FXOMObject> fxomObjects);

    ClipboardContent makeEncoding(List<? extends FXOMObject> fxomObjects);

}