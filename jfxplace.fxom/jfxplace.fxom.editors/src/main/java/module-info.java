import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.treilhes.jfxplace.fxom.editors.FxomEditorsExtension;

open module jfxplace.fxom.editors {

    exports com.treilhes.jfxplace.fxom.editors;
    exports com.treilhes.jfxplace.fxom.editors.api;
    exports com.treilhes.jfxplace.fxom.editors.base;

    exports com.treilhes.jfxplace.fxom.editors.control;
    exports com.treilhes.jfxplace.fxom.editors.control.effectpicker.editors;

    exports com.treilhes.jfxplace.fxom.editors.popupeditors;
    exports com.treilhes.jfxplace.fxom.editors.control.effectpicker;

    exports com.treilhes.jfxplace.fxom.editors.util;

    requires transitive jfxplace.core.api;
    requires jfxplace.core.starter;
    requires jfxplace.fxom.metadata;
    requires emc4j.boot.api;


    provides Extension with FxomEditorsExtension;
}