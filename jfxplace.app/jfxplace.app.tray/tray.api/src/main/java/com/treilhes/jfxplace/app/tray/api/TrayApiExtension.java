package com.treilhes.jfxplace.app.tray.api;

import java.util.List;
import java.util.UUID;

import com.treilhes.jfxplace.core.api.JfxplaceCoreApiExtension;
import com.treilhes.emc4j.boot.api.loader.extension.ApplicationExtension;

public class TrayApiExtension implements ApplicationExtension {

    public static final UUID ID = JfxplaceCoreApiExtension.TRAY_APP_ID;

    @Override
    public UUID getId() {
        return ID;
    }

    @Override
    public List<Class<?>> localContextClasses() {
        return List.of();
    }

}
