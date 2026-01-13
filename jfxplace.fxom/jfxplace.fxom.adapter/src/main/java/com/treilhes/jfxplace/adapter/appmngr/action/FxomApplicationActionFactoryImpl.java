package com.treilhes.jfxplace.adapter.appmngr.action;

import com.gluonhq.jfxapps.core.api.action.Action;
import com.gluonhq.jfxapps.core.api.action.ActionFactory;
import com.gluonhq.jfxapps.core.api.fxom.action.FxomApplicationActionFactory;

public class FxomApplicationActionFactoryImpl implements FxomApplicationActionFactory {

    private final ActionFactory actionFactory;

    public FxomApplicationActionFactoryImpl(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public Action closeInstance(boolean force) {
        return actionFactory.create(FxomCloseInstanceAction.class, a -> a.setForce(force));
    }
}
