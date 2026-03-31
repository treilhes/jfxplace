package com.gluonhq.jfxapps.core.api.fxom.editor.selection;

import com.treilhes.jfxplace.core.api.action.Action;

public interface SelectionActionsFactory {

    Action copy();

    Action cut();

    Action delete();

    Action duplicate();

    Action paste();

    Action pasteInto();

    Action selectAll();

    Action selectNext();

    Action selectNone();

    Action selectParent();

    Action selectPrevious();

    Action trim();

    Action bringForward();

    Action sendBackward();

    Action bringToFront();

    Action sendToBack();

}