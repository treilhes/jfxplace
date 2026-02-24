package com.gluonhq.jfxapps.core.api.fxom.editor.selection;

import com.gluonhq.jfxapps.core.api.action.Action;

public interface FxomSelectionActionsFactory {

    Action copy();

    Action cut();

    Action delete();

    Action duplicate();

    Action paste();

    Action pasteInto();

    Action trim();

    Action bringForward();

    Action sendBackward();

    Action bringToFront();

    Action sendToBack();

}
