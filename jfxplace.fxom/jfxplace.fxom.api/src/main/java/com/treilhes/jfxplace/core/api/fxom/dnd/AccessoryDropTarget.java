package com.treilhes.jfxplace.core.api.fxom.dnd;

import java.util.List;

import com.treilhes.jfxplace.core.api.fxom.mask.Accessory;
import com.treilhes.jfxplace.core.api.job.Job;
import com.treilhes.jfxplace.core.fxom.FXOMObject;

public interface AccessoryDropTarget extends DropTarget {

    Accessory getAccessory();

    FXOMObject getBeforeChild();

    Accessory findTargetAccessory(List<? extends FXOMObject> draggedObject);

}