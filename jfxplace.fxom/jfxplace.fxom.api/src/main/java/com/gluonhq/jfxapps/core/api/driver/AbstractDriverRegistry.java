package com.gluonhq.jfxapps.core.api.driver;

import java.util.HashMap;
import java.util.Map;

import com.gluonhq.jfxapps.util.InheritanceMap;
import com.treilhes.emc4j.boot.api.context.EmContext;

public class AbstractDriverRegistry implements DriverRegistry {

    private final EmContext context;
    private final Map<Class<?>, InheritanceMap<?>> extensions = new HashMap<>();

    public AbstractDriverRegistry(EmContext context) {
        super();
        this.context = context;
    }

    @Override
    public <U> void registerExtension(Class<U> extensionInterface) {
        if (!extensions.containsKey(extensionInterface)) {
            extensions.put(extensionInterface, new InheritanceMap<U>());
        }
    }

    @Override
    public <T, U extends T> void registerImplementationClass(Class<T> extensionInterface, Class<?> itemClass,
            Class<U> implementation) {
        assert extensions.containsKey(extensionInterface);

        @SuppressWarnings("unchecked")
        InheritanceMap<T> ef = (InheritanceMap<T>) extensions.get(extensionInterface);

        ef.put(itemClass, implementation);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, U extends T> Class<U> getImplementationClass(Class<T> extensionInterface, Class<?> itemClass) {
        assert extensions.containsKey(extensionInterface);

        InheritanceMap<T> ef = (InheritanceMap<T>) extensions.get(extensionInterface);

        return (Class<U>) ef.getFirstInherited(itemClass);
    }

    @Override
    public <T, U extends T> U getImplementationInstance(Class<T> extensionInterface, Class<?> itemClass) {
        Class<U> uClass = getImplementationClass(extensionInterface, itemClass);

        if (uClass == null) {
            return null;
        }

        return context.getBean(uClass);
    }
}
