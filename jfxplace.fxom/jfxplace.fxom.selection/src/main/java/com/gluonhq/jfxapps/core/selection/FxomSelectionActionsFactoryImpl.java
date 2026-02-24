package com.gluonhq.jfxapps.core.selection;

import com.gluonhq.jfxapps.core.api.action.Action;
import com.gluonhq.jfxapps.core.api.action.ActionFactory;
import com.gluonhq.jfxapps.core.api.fxom.editor.selection.FxomSelectionActionsFactory;
import com.gluonhq.jfxapps.core.selection.action.BringForwardAction;
import com.gluonhq.jfxapps.core.selection.action.BringToFrontAction;
import com.gluonhq.jfxapps.core.selection.action.CopyAction;
import com.gluonhq.jfxapps.core.selection.action.CutAction;
import com.gluonhq.jfxapps.core.selection.action.DeleteAction;
import com.gluonhq.jfxapps.core.selection.action.DuplicateAction;
import com.gluonhq.jfxapps.core.selection.action.PasteAction;
import com.gluonhq.jfxapps.core.selection.action.PasteIntoAction;
import com.gluonhq.jfxapps.core.selection.action.SendBackwardAction;
import com.gluonhq.jfxapps.core.selection.action.SendToBackAction;
import com.gluonhq.jfxapps.core.selection.action.TrimAction;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;

@ApplicationSingleton
public class FxomSelectionActionsFactoryImpl implements FxomSelectionActionsFactory {

    private final ActionFactory actionFactory;

    public FxomSelectionActionsFactoryImpl(ActionFactory actionFactory) {
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
