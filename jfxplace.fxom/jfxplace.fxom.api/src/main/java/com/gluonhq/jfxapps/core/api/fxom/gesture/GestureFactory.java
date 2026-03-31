package com.gluonhq.jfxapps.core.api.fxom.gesture;

import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.jfxplace.core.api.factory.AbstractFactory;

public class GestureFactory<T extends Gesture> extends AbstractFactory<T> {

    public GestureFactory(EmContext sbContext) {
        super(sbContext);
    }

}
