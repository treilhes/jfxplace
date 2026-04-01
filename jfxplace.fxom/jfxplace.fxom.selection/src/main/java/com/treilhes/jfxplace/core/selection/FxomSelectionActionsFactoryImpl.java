package com.treilhes.jfxplace.core.selection;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.jfxplace.core.api.action.Action;
import com.treilhes.jfxplace.core.api.action.ActionFactory;
import com.treilhes.jfxplace.core.api.fxom.editor.selection.FxomSelectionActionsFactory;
import com.treilhes.jfxplace.core.selection.action.BringForwardAction;
import com.treilhes.jfxplace.core.selection.action.BringToFrontAction;
import com.treilhes.jfxplace.core.selection.action.CopyAction;
import com.treilhes.jfxplace.core.selection.action.CutAction;
import com.treilhes.jfxplace.core.selection.action.DeleteAction;
import com.treilhes.jfxplace.core.selection.action.DuplicateAction;
import com.treilhes.jfxplace.core.selection.action.PasteAction;
import com.treilhes.jfxplace.core.selection.action.PasteIntoAction;
import com.treilhes.jfxplace.core.selection.action.SendBackwardAction;
import com.treilhes.jfxplace.core.selection.action.SendToBackAction;
import com.treilhes.jfxplace.core.selection.action.TrimAction;

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
