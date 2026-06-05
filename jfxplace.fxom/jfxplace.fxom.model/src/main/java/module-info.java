import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.treilhes.jfxplace.core.fxom.FxomModelExtension;
import com.treilhes.jfxplace.core.fxom.ext.FXOMNormalizer;
import com.treilhes.jfxplace.core.fxom.ext.FXOMRefresher;
import com.treilhes.jfxplace.core.fxom.ext.FileLoader;
import com.treilhes.jfxplace.core.fxom.ext.LoaderCapabilitiesManager;
import com.treilhes.jfxplace.core.fxom.ext.TransientStateBackup;
import com.treilhes.jfxplace.core.fxom.ext.WeakProperty;

open module jfxplace.fxom.model {

    exports com.treilhes.jfxplace.core.fxom;
    exports com.treilhes.jfxplace.core.fxom.collector;
    exports com.treilhes.jfxplace.core.fxom.glue;
    exports com.treilhes.jfxplace.core.fxom.ext;
    exports com.treilhes.jfxplace.core.fxom.pipeline;
    exports com.treilhes.jfxplace.core.fxom.util;

    requires transitive jfxplace.javafx.starter;
    requires transitive jfxplace.core.api;
    requires transitive emc4j.boot.starter;
    requires jfxplace.javafx.fxml.patch.link;
    requires emc4j.boot.api;


    provides Extension with FxomModelExtension;

    uses FXOMNormalizer;
    uses FXOMRefresher;
    uses TransientStateBackup;
    uses WeakProperty;
    uses FileLoader;
    uses LoaderCapabilitiesManager;

}