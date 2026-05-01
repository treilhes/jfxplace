package com.treilhes.jfxplace.core.fxom.ext;

import com.treilhes.jfxplace.core.fxom.FXOMObject;

public interface TransientStateBackup {

    boolean canHandle(FXOMObject candidate);
    void backup(FXOMObject candidate);
    void restore(FXOMObject candidate); 
    
}
