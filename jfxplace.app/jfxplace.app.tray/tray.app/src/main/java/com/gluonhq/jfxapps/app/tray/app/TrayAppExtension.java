package com.gluonhq.jfxapps.app.tray.app;

import java.util.List;
import java.util.UUID;

import com.gluonhq.jfxapps.app.tray.api.TrayApiExtension;
import com.gluonhq.jfxapps.app.tray.app.init.TrayOpenCommandEventHandler;
import com.gluonhq.jfxapps.app.tray.app.init.TrayUi;
import com.gluonhq.jfxapps.app.tray.app.init.WindowIconSettings;
import com.treilhes.emc4j.boot.api.loader.extension.OpenExtension;

public class TrayAppExtension implements OpenExtension {

    private static final UUID ID = UUID.fromString("45c6997b-07b3-4155-a8d2-5a1d8e0fd4ab");

    @Override
    public UUID getId() {
        return ID;
    }

    @Override
    public UUID getParentId() {
        return TrayApiExtension.ID;
    }

    @Override
    public List<Class<?>> localContextClasses() {
        return List.of();
    }

    @Override
    public List<Class<?>> exportedContextClasses() {
        return List.of(
                TrayOpenCommandEventHandler.class,
                WindowIconSettings.class,
                TrayUi.class
                );
    }

}
