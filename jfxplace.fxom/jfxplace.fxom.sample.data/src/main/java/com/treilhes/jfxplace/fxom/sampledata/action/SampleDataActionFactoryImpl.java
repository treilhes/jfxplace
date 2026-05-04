package com.treilhes.jfxplace.fxom.sampledata.action;

import com.treilhes.jfxplace.core.api.action.Action;
import com.treilhes.jfxplace.core.api.action.ActionFactory;
import com.treilhes.jfxplace.core.fxom.sample.SampleDataActionFactory;

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
