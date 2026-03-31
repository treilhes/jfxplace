package com.treilhes.jfxplace.core.selection;

import org.springframework.context.annotation.Fallback;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.api.selection.EmptySelectionGroupFactory;
import com.treilhes.jfxplace.core.api.selection.SelectionGroup;

@ApplicationInstanceSingleton
@Fallback
public class EmptySelectionGroupFactoryImpl implements EmptySelectionGroupFactory {

    @Override
    public SelectionGroup empty() {
        // TODO Auto-generated method stub
        return null;
    }

}
