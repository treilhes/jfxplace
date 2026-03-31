package com.treilhes.jfxplace.core.api.selection;

import com.treilhes.jfxplace.core.api.action.Action;

public interface SelectionActionsFactory {

    Action selectAll();

    Action selectNext();

    Action selectNone();

    Action selectParent();

    Action selectPrevious();

}