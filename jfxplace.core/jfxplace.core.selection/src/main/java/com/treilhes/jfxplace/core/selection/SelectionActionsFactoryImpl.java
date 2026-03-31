package com.treilhes.jfxplace.core.selection;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.jfxplace.core.api.action.Action;
import com.treilhes.jfxplace.core.api.action.ActionFactory;
import com.treilhes.jfxplace.core.api.selection.SelectionActionsFactory;
import com.treilhes.jfxplace.core.selection.action.SelectAllAction;
import com.treilhes.jfxplace.core.selection.action.SelectNextAction;
import com.treilhes.jfxplace.core.selection.action.SelectNoneAction;
import com.treilhes.jfxplace.core.selection.action.SelectParentAction;
import com.treilhes.jfxplace.core.selection.action.SelectPreviousAction;

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
