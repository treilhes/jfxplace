package com.treilhes.jfxplace.core.api.fxom.clipboard;

public interface ClipboardHandler {
    
    public void performCopy();
    public void performCut();
    public void performPaste();
    
    public boolean canPerformCopy();
    public boolean canPerformCut();
    public boolean canPerformPaste();
    
}
