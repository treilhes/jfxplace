package javafx.fxml;

import java.util.HashMap;

import com.sun.javafx.fxml.BeanAdapter;

public class BeanAdapterGlobalCacheCleaner {

    public static void clean(ModuleLayer layer) {
        Class<BeanAdapter> beanAdapterClass = BeanAdapter.class;
        try {
            // Access the HashMap<Class<?>, MethodCache> globalMethodCache globalMethodCache
            // static field of BeanAdapter
            var globalMethodCacheField = beanAdapterClass.getDeclaredField("globalMethodCache");
            globalMethodCacheField.setAccessible(true);

            var globalMethodCache = (HashMap<Class<?>, ?>) globalMethodCacheField.get(null);
            // remove all class keys owned by the layer
            globalMethodCache.entrySet().removeIf(entry -> {
                var clazz = entry.getKey();
                return clazz.getModule().getLayer() == layer;
            });

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}