package com.treilhes.jfxplace.app.tray.app;

import java.util.List;
import java.util.UUID;

import com.treilhes.emc4j.boot.api.layer.Layer;
import com.treilhes.emc4j.boot.api.loader.extension.OpenExtension;
import com.treilhes.jfxplace.app.tray.api.TrayApiExtension;
import com.treilhes.jfxplace.app.tray.app.init.TrayOpenCommandEventHandler;
import com.treilhes.jfxplace.app.tray.app.init.TrayUi;
import com.treilhes.jfxplace.app.tray.app.init.WindowIconSettings;
import com.treilhes.jfxplace.app.tray.app.menu.TrayMenuManager;

public class TrayAppExtension implements OpenExtension {

    private static final UUID ID = UUID.fromString("45c6997b-07b3-4155-a8d2-5a1d8e0fd4ab");

    @Override
    public void initializeModule(Layer layer) {
        var moduleLayer = layer.getModuleLayer();
        var controller = layer.getModuleController();

        var d = moduleLayer.findModule("dorkbox.utilities");
        var a = moduleLayer.findModule("java.desktop");

        if (d.isPresent() && a.isPresent()) {
            controller.addReads(d.get(), a.get());
        }

        var tray = moduleLayer.findModule("dorkbox.systemtray");
        controller.addExports(tray.get(), "dorkbox.systemTray.ui.swing", TrayAppExtension.class.getModule());
    }

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
                TrayUi.class,
                TrayMenuManager.class
                );
    }

}
