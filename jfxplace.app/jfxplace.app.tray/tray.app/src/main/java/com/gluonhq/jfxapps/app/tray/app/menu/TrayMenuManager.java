package com.gluonhq.jfxapps.app.tray.app.menu;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gluonhq.jfxapps.app.tray.app.utils.SystemTrayJavaFxProvider;
import com.gluonhq.jfxapps.core.api.JfxplaceCoreApiExtension;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.loader.ApplicationManager;
import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;
import com.treilhes.emc4j.boot.api.registry.RegistryManager;
import com.treilhes.emc4j.boot.api.registry.model.ApplicationInfo;

import dorkbox.jna.rendering.RenderProvider;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.CacheUtil;

@ApplicationSingleton
public class TrayMenuManager {

    private static final Logger logger = LoggerFactory.getLogger(TrayMenuManager.class);
    private static final String TRAY_ID = "JfxPlaceTray";
    private static final boolean DEBUG = true;

    private final ApplicationManager applicationManager;
    private final RegistryManager registryManager;

    // @formatter:off
    public TrayMenuManager(
            ApplicationManager applicationManager,
            RegistryManager registryManager
            ) {
     // @formatter:on
        super();
        this.applicationManager = applicationManager;
        this.registryManager = registryManager;
    }

    public void init() {
        RenderProvider.set(new SystemTrayJavaFxProvider());

        SystemTray.DEBUG = DEBUG;
        new CacheUtil(TRAY_ID).clear();

        // Get a SystemTray instance (auto-detects native or fallback)
        SystemTray systemTray = SystemTray.get(TRAY_ID);

        if (systemTray == null) {
            logger.error("Unable to load SystemTray!");
            return;
        }

        var iconUrl = this.getClass().getResource("icon.png");
        // Set the image/icon for the tray
        systemTray.setImage(iconUrl);
        // Optional: set a status text
        //systemTray.setStatus("App Running");

        // Add some menu items
        systemTray.getMenu().add(new MenuItem("Manage", (ActionEvent e) -> {
            var mngrId = JfxplaceCoreApiExtension.MANAGER_APP_ID;
            applicationManager.startApplication(mngrId);
            applicationManager.send(new OpenCommandEvent(mngrId, List.of()));
        }));

        var startMenu = new Menu("Start");
        systemTray.getMenu().add(startMenu);

        var restartMenu = new Menu("Restart");
        systemTray.getMenu().add(restartMenu);

        var stopMenu = new Menu("Stop");
        systemTray.getMenu().add(stopMenu);

        systemTray.getMenu().add(new MenuItem("Force Quit", (ActionEvent e) -> {
            systemTray.shutdown();  // removes the tray icon
            System.exit(0);
        }));

        TrayMouseListener trayListener = new TrayMouseListener((evt, src) -> {

            if (src == startMenu && evt.getID() == MouseEvent.MOUSE_ENTERED) {
                startMenu.getEntries().forEach(e -> startMenu.remove(e));
                registryManager.listApplicationsInfo().stream()
                    .filter(ApplicationInfo::isInstalled)
                    .filter(Predicate.not(ApplicationInfo::isDaemon))
                    .filter(app -> !applicationManager.isStarted(app.getUuid()))
                    .forEach(app -> {
                        startMenu.add(new MenuItem(app.getTitle(), (ActionEvent e) -> {
                            applicationManager.startApplication(app.getUuid());
                            applicationManager.send(new OpenCommandEvent(app.getUuid(), List.of()));
                        }));
                    });
            }

            if (src == restartMenu && evt.getID() == MouseEvent.MOUSE_ENTERED) {
                restartMenu.getEntries().forEach(e -> restartMenu.remove(e));
                registryManager.listApplicationsInfo().stream()
                    .filter(ApplicationInfo::isInstalled)
                    .filter(Predicate.not(ApplicationInfo::isDaemon))
                    .filter(app -> applicationManager.isStarted(app.getUuid()))
                    .forEach(app -> {
                        restartMenu.add(new MenuItem(app.getTitle(), (ActionEvent e) -> {
                            applicationManager.startApplication(app.getUuid());
                            applicationManager.send(new OpenCommandEvent(app.getUuid(), List.of()));
                        }));
                    });
            }

            if (src == stopMenu && evt.getID() == MouseEvent.MOUSE_ENTERED) {
                stopMenu.getEntries().forEach(e -> stopMenu.remove(e));
                registryManager.listApplicationsInfo().stream()
                    .filter(ApplicationInfo::isInstalled)
                    .filter(Predicate.not(ApplicationInfo::isDaemon))
                    .filter(app -> applicationManager.isStarted(app.getUuid()))
                    .forEach(app -> {
                        stopMenu.add(new MenuItem(app.getTitle(), (ActionEvent e) -> {
                            applicationManager.stopApplication(app.getUuid());
                        }));
                    });
            }

        });

        // TODO: need to test this on macos and linux
        SystemTray.SWING_UI = new UiFactoryWrapper(SystemTray.SWING_UI, trayListener);

    }
}
