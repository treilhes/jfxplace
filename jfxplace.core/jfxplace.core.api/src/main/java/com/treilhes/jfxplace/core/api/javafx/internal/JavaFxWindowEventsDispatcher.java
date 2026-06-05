package com.treilhes.jfxplace.core.api.javafx.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.jfxplace.core.api.javafx.JavaFxWindowOwnerRegistry;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class JavaFxWindowEventsDispatcher  implements EventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(JavaFxWindowEventsDispatcher.class);

    private final JavaFxWindowOwnerRegistry windowOwnerRegistry;
    private final Window window;
    private final JavafxThreadClassloaderDispatcher dispatcher;
    private EventDispatcher originalDispatcher;

    public JavaFxWindowEventsDispatcher(JavaFxWindowOwnerRegistry windowOwnerRegistry,Window window, JavafxThreadClassloaderDispatcher dispatcher) {
        this.windowOwnerRegistry = windowOwnerRegistry;
        this.window = window;
        this.dispatcher = dispatcher;
        this.originalDispatcher = window.getEventDispatcher();
    }

    private Window windowFromSource(Object source) {
        Window window = switch (source) {
            case null -> null;
            case Node o -> windowFromSource(o.getScene().getWindow());
            case Scene o -> windowFromSource(o.getWindow());
            case MenuItem o -> windowFromSource(o.getParentPopup());
            case PopupWindow o -> windowFromSource(o.getOwnerWindow());
            case Window o -> o;
            default -> null;
        };

        return window == null ? null : window.getScene().getWindow();
    }
    @Override
    public Event dispatchEvent(Event event, EventDispatchChain tail) {

        // sometimes the dispatcher is not set on the window when instanciated, we need to set it to ensure we can intercept events
        if (originalDispatcher == null && window.getEventDispatcher() != null) {
            originalDispatcher = window.getEventDispatcher();
            window.setEventDispatcher(this);
        }

        Window sourceWindow = windowFromSource(event.getTarget());

        if (sourceWindow == null) {
            sourceWindow = windowFromSource(window);
        }

        try {
            if (event instanceof WindowEvent we && we.getEventType() == WindowEvent.WINDOW_HIDDEN
                    && window == we.getSource() && window == we.getTarget()) {
                tail.append((e, t) -> {
                    windowOwnerRegistry.terminate(window);
                    return e;
                });
            }

            return dispatcher.callWith(sourceWindow, () -> {
                return originalDispatcher.dispatchEvent(event, tail);
            });
        } catch (Exception e) {
            log.error("Error dispatching event", event, e);
        }

        return null;
    }
}
