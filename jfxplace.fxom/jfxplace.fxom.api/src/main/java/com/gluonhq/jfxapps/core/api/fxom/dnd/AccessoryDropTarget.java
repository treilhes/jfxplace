package com.gluonhq.jfxapps.core.api.fxom.dnd;

import java.util.List;

import com.gluonhq.jfxapps.core.api.fxom.mask.Accessory;
import com.gluonhq.jfxapps.core.api.job.Job;
import com.gluonhq.jfxapps.core.fxom.FXOMObject;

public interface AccessoryDropTarget extends DropTarget {

    Accessory getAccessory();

    FXOMObject getBeforeChild();

    Accessory findTargetAccessory(List<? extends FXOMObject> draggedObject);

}