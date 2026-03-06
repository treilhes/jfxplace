package com.gluonhq.jfxapps.core.api.driver;

import org.springframework.context.annotation.Fallback;

import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;

@ApplicationSingleton
@Fallback
public class DefaultDriverRegistry extends AbstractDriverRegistry {

    public DefaultDriverRegistry(EmContext context) {
        super(context);
    }

}
