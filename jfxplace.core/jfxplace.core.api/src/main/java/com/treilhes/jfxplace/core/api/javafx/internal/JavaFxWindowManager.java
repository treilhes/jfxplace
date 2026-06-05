package com.treilhes.jfxplace.core.api.javafx.internal;

import com.treilhes.emc4j.boot.api.context.annotation.Singleton;
import com.treilhes.jfxplace.core.api.javafx.JavaFxWindowOwnerRegistry;

import javafx.collections.ListChangeListener.Change;
import javafx.stage.Stage;
import javafx.stage.Window;

@Singleton
public class JavaFxWindowManager {

    private final JavaFxWindowOwnerRegistry windowOwnerRegistry;
    private final JavafxThreadClassloaderDispatcher fxThreadClassloaderDispatcher;
    private JavaFxWindowEventsDispatcher primaryDispatcher;

    protected JavaFxWindowManager(JavaFxWindowOwnerRegistry windowOwnerRegistry, JavafxThreadClassloaderDispatcher fxThreadClassloaderDispatcher) {
        this.windowOwnerRegistry = windowOwnerRegistry;
        this.fxThreadClassloaderDispatcher = fxThreadClassloaderDispatcher;
    }

    public void start(Stage primaryStage) {
        primaryDispatcher = new JavaFxWindowEventsDispatcher(windowOwnerRegistry, primaryStage, fxThreadClassloaderDispatcher);
//        primaryStage.setEventDispatcher(primaryDispatcher);

        Window.getWindows().addListener((Change<? extends Window> c) -> {
            while (c.next()) {
                c.getAddedSubList().forEach( w -> {
                    var windowDispatcher = new JavaFxWindowEventsDispatcher(windowOwnerRegistry, w, fxThreadClassloaderDispatcher);
//                    w.setEventDispatcher(windowDispatcher);
                    fxThreadClassloaderDispatcher.listenFocus(w);
                });

                c.getRemoved().forEach(w -> {
                    w.setEventDispatcher(null);
                    windowOwnerRegistry.terminate(w);
                });
            }
        });
    }
}
