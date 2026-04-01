package com.treilhes.jfxplace.core.api.fxom.dnd;

import com.treilhes.jfxplace.core.api.fxom.job.base.BatchJob;
import com.treilhes.jfxplace.core.fxom.FXOMInstance;

public interface DropJobCustomizer {
    void customize(BatchJob job, FXOMInstance draggedObject);
}
