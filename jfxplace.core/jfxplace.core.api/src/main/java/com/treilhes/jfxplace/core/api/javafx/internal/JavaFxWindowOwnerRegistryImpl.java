package com.treilhes.jfxplace.core.api.javafx.internal;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.emc4j.boot.api.context.annotation.Singleton;
import com.treilhes.jfxplace.core.api.application.Application;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.javafx.JavaFxWindowOwnerRegistry;

import javafx.stage.Stage;
import javafx.stage.Window;

@Singleton
public class JavaFxWindowOwnerRegistryImpl implements JavaFxWindowOwnerRegistry {

    private static final Logger log = LoggerFactory.getLogger(JavaFxWindowOwnerRegistryImpl.class);

    private final Map<Window, ApplicationInstance> windowToInstance = new ConcurrentHashMap<>();

    private final Map<Window, Application> windowToApplication = new ConcurrentHashMap<>();

    private final Map<Predicate<Window>[], ApplicationInstance> predicatesToInstance = new ConcurrentHashMap<>();

    private final Map<Predicate<Window>[], Application> predicatesToApplication = new ConcurrentHashMap<>();

    JavaFxWindowOwnerRegistryImpl() {

    }

    @Override
    public void register(Window window, ApplicationInstance instance) {
        log.info("Registering window {} with instance {}", window, instance);
        windowToInstance.put(window, instance);
    }

    @Override
    public void register(Window window, Application application) {
        log.info("Registering window {} with application {}", window, application);
        windowToApplication.put(window, application);
    }

    @Override
    public void registerWithNextWindow(Application application, Predicate<Window>... predicates) {
        if (application == null) {
            throw new IllegalArgumentException("application cannot be null");
        }
        predicatesToApplication.put(predicates, application);
    }

    @Override
    public void registerWithNextWindow(ApplicationInstance instance, Predicate<Window>... predicates) {
        if (instance == null) {
            throw new IllegalArgumentException("instance cannot be null");
        }
        predicatesToInstance.put(predicates, instance);
    }

    @Override
    public void terminate(ApplicationInstance instance) {
        var removableWin = windowToInstance.entrySet().stream().filter(entry -> entry.getValue() == instance)
                .map(Map.Entry::getKey).toList();
        var removablePreds = predicatesToInstance.entrySet().stream().filter(entry -> entry.getValue() == instance)
                .map(Map.Entry::getKey).toList();

        removableWin.forEach(windowToInstance::remove);
        removablePreds.forEach(predicatesToInstance::remove);
    }

    @Override
    public void terminate(Application application) {
        var removableWin = windowToApplication.entrySet().stream().filter(entry -> entry.getValue() == application)
                .map(Map.Entry::getKey).toList();
        var removablePreds = predicatesToApplication.entrySet().stream().filter(entry -> entry.getValue() == application)
                .map(Map.Entry::getKey).toList();
        var removableInstanceWin = windowToInstance.entrySet().stream().filter(entry -> entry.getValue().getApplication() == application)
                .map(Map.Entry::getKey).toList();
        var removableInstancePreds = predicatesToInstance.entrySet().stream().filter(entry -> entry.getValue().getApplication() == application)
                .map(Map.Entry::getKey).toList();

        removableWin.forEach(windowToApplication::remove);
        removablePreds.forEach(predicatesToApplication::remove);
        removableInstanceWin.forEach(windowToInstance::remove);
        removableInstancePreds.forEach(predicatesToInstance::remove);
    }

    @Override
    public void terminate(Window window) {
        log.info("Unregistering window {}", window);
        windowToInstance.remove(window);
        windowToApplication.remove(window);
    }

    public ClassLoader classloaderOf(Window window) {
        Window owner = ownerWindowOf(window);
        if (windowToApplication.containsKey(owner)) {
            return windowToApplication.get(owner).getContext().getBeanClassLoader();
        } else if (windowToInstance.containsKey(owner)) {
            return windowToInstance.get(owner).getContext().getBeanClassLoader();
        } else {
            throw new IllegalArgumentException("Window not registered: " + window);
        }
    }

    public void tryRegisterFromPredicates(Window window) {
        if (windowToApplication.containsKey(window) || windowToInstance.containsKey(window)) {
            return;
        }

        if (!predicatesToInstance.isEmpty()) {
            predicatesToInstance.forEach((predicates, instance) -> {
                boolean match = Arrays.stream(predicates).allMatch(p -> p.test(window));
                if (match && predicatesToInstance.remove(predicates, instance)) {
                    register(window, instance);
                    return;
                }
            });
        }

        if (!predicatesToApplication.isEmpty()) {
            predicatesToApplication.forEach((predicates, application) -> {
                boolean match = Arrays.stream(predicates).allMatch(p -> p.test(window));
                if (match && predicatesToApplication.remove(predicates, application)) {
                    register(window, application);
                    return;
                }
            });
        }
    }

    private static Window ownerWindowOf(Window window) {
        if (window instanceof Stage stage && stage.getOwner() != null) {
            return ownerWindowOf(stage.getOwner());
        }
        return window;
    }

}
