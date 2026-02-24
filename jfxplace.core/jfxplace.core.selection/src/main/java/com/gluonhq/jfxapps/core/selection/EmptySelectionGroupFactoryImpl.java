package com.gluonhq.jfxapps.core.selection;

import org.springframework.context.annotation.Fallback;

import com.gluonhq.jfxapps.core.api.selection.EmptySelectionGroupFactory;
import com.gluonhq.jfxapps.core.api.selection.SelectionGroup;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;

@ApplicationInstanceSingleton
@Fallback
public class EmptySelectionGroupFactoryImpl implements EmptySelectionGroupFactory {

    @Override
    public SelectionGroup empty() {
        // TODO Auto-generated method stub
        return null;
    }

}
