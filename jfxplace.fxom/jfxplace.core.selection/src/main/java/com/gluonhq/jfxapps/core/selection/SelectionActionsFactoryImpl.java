package com.gluonhq.jfxapps.core.selection;

import com.gluonhq.jfxapps.core.api.action.Action;
import com.gluonhq.jfxapps.core.api.action.ActionFactory;
import com.gluonhq.jfxapps.core.api.fxom.editor.selection.SelectionActionsFactory;
import com.gluonhq.jfxapps.core.selection.action.BringForwardAction;
import com.gluonhq.jfxapps.core.selection.action.BringToFrontAction;
import com.gluonhq.jfxapps.core.selection.action.CopyAction;
import com.gluonhq.jfxapps.core.selection.action.CutAction;
import com.gluonhq.jfxapps.core.selection.action.DeleteAction;
import com.gluonhq.jfxapps.core.selection.action.DuplicateAction;
import com.gluonhq.jfxapps.core.selection.action.PasteAction;
import com.gluonhq.jfxapps.core.selection.action.PasteIntoAction;
import com.gluonhq.jfxapps.core.selection.action.SelectAllAction;
import com.gluonhq.jfxapps.core.selection.action.SelectNextAction;
import com.gluonhq.jfxapps.core.selection.action.SelectNoneAction;
import com.gluonhq.jfxapps.core.selection.action.SelectParentAction;
import com.gluonhq.jfxapps.core.selection.action.SelectPreviousAction;
import com.gluonhq.jfxapps.core.selection.action.SendBackwardAction;
import com.gluonhq.jfxapps.core.selection.action.SendToBackAction;
import com.gluonhq.jfxapps.core.selection.action.TrimAction;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;

@ApplicationSingleton
public class SelectionActionsFactoryImpl implements SelectionActionsFactory {

    private final ActionFactory actionFactory;

    public SelectionActionsFactoryImpl(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public Action copy() {
        return actionFactory.create(CopyAction.class);
    }

    @Override
    public Action cut() {
        return actionFactory.create(CutAction.class);
    }

    @Override
    public Action delete() {
        return actionFactory.create(DeleteAction.class);
    }

    @Override
    public Action duplicate() {
        return actionFactory.create(DuplicateAction.class);
    }

    @Override
    public Action paste() {
        return actionFactory.create(PasteAction.class);
    }

    @Override
    public Action pasteInto() {
        return actionFactory.create(PasteIntoAction.class);
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

    @Override
    public Action trim() {
        return actionFactory.create(TrimAction.class);
    }

    @Override
    public Action bringForward() {
        return actionFactory.create(BringForwardAction.class);
    }

    @Override
    public Action sendBackward() {
        return actionFactory.create(SendBackwardAction.class);
    }

    @Override
    public Action bringToFront() {
        return actionFactory.create(BringToFrontAction.class);
    }

    @Override
    public Action sendToBack() {
        return actionFactory.create(SendToBackAction.class);
    }
}
