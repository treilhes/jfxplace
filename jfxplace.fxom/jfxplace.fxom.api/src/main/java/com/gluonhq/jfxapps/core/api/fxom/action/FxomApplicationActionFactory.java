package com.gluonhq.jfxapps.core.api.fxom.action;

import com.gluonhq.jfxapps.core.api.action.Action;

public interface FxomApplicationActionFactory {

    Action closeInstance(boolean force);

}