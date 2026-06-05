package com.treilhes.jfxplace.fxom.sampledata.action;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.api.action.Action;
import com.treilhes.jfxplace.core.api.instance.ActionFactory;
import com.treilhes.jfxplace.core.fxom.sample.SampleDataActionFactory;

@ApplicationInstanceSingleton
public class SampleDataActionFactoryImpl implements SampleDataActionFactory {

    private final ActionFactory actionFactory;

    public SampleDataActionFactoryImpl(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public Action toggleSampleData() {
        return actionFactory.create(ToggleSampleDataAction.class);
    }

}
