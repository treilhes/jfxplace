import com.gluonhq.jfxapps.app.tray.api.TrayApiExtension;
import com.treilhes.emc4j.boot.api.loader.extension.Extension;

open module tray.api {
    exports com.gluonhq.jfxapps.app.tray.api;

    requires jfxplace.core.api;

    provides Extension with TrayApiExtension;
}