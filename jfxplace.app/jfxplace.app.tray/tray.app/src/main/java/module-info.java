import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.treilhes.jfxplace.app.tray.app.TrayAppExtension;

open module tray.app {
    exports com.treilhes.jfxplace.app.tray.app;

    requires tray.api;
    requires jfxplace.core.api;
    requires java.desktop;
    requires dorkbox.systemtray;
    requires dorkbox.utilities;

    provides Extension with TrayAppExtension;
}