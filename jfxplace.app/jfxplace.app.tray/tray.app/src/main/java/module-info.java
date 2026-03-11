import com.gluonhq.jfxapps.app.tray.app.TrayAppExtension;
import com.treilhes.emc4j.boot.api.loader.extension.Extension;

open module tray.app {
    exports com.gluonhq.jfxapps.app.tray.app;

    requires tray.api;
    requires jfxplace.core.api;
    requires dorkbox.systemtray;
    requires java.desktop;
    requires dorkbox.utilities;

    provides Extension with TrayAppExtension;
}