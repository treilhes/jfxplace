package com.gluonhq.jfxapps.core.api.selection;

import com.gluonhq.jfxapps.core.api.action.Action;

public interface SelectionActionsFactory {

    Action selectAll();

    Action selectNext();

    Action selectNone();

    Action selectParent();

    Action selectPrevious();

}