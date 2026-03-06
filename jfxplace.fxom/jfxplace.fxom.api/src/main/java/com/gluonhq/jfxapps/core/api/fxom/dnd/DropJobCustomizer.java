package com.gluonhq.jfxapps.core.api.fxom.dnd;

import com.gluonhq.jfxapps.core.api.fxom.job.base.BatchJob;
import com.gluonhq.jfxapps.core.fxom.FXOMInstance;

public interface DropJobCustomizer {
    void customize(BatchJob job, FXOMInstance draggedObject);
}
