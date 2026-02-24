package com.gluonhq.jfxapps.core.selection;

import com.gluonhq.jfxapps.core.api.action.Action;
import com.gluonhq.jfxapps.core.api.action.ActionFactory;
import com.gluonhq.jfxapps.core.api.selection.SelectionActionsFactory;
import com.gluonhq.jfxapps.core.selection.action.SelectAllAction;
import com.gluonhq.jfxapps.core.selection.action.SelectNextAction;
import com.gluonhq.jfxapps.core.selection.action.SelectNoneAction;
import com.gluonhq.jfxapps.core.selection.action.SelectParentAction;
import com.gluonhq.jfxapps.core.selection.action.SelectPreviousAction;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;

@ApplicationSingleton
public class SelectionActionsFactoryImpl implements SelectionActionsFactory {

    private final ActionFactory actionFactory;

    public SelectionActionsFactoryImpl(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public Action selectAll() {
        return actionFactory.create(SelectAllAction.class);
    }

    @Override
    public Action selectNext() {
        return actionFactory.create(SelectNextAction.class);
    }

    @Override
    public Action selectNone() {
        return actionFactory.create(SelectNoneAction.class);
    }

    @Override
    public Action selectParent() {
        return actionFactory.create(SelectParentAction.class);
    }

    @Override
    public Action selectPrevious() {
        return actionFactory.create(SelectPreviousAction.class);
    }

}
