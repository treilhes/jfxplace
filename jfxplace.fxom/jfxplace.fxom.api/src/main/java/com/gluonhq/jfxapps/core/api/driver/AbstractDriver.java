package com.gluonhq.jfxapps.core.api.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDriver implements Driver {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDriver.class);

    private final DriverRegistry registry;

    public AbstractDriver(DriverRegistry registry) {
        super();
        this.registry = registry;
    }

    @Override
    public <T> T make(Class<T> cls, Class<?> targetClass) {
        if (targetClass == null) {
            logger.warn("Cannot make a anything for null class");
            return null;
        }
        return registry.getImplementationInstance(cls,targetClass);
    }

}
