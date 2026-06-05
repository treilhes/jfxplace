import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.treilhes.jfxplace.core.metadata.FxomMetadataExtension;

module jfxplace.fxom.metadata {

    exports com.treilhes.jfxplace.core.metadata;
    exports com.treilhes.jfxplace.core.metadata.component;
    exports com.treilhes.jfxplace.core.metadata.klass;
    exports com.treilhes.jfxplace.core.metadata.property;
    exports com.treilhes.jfxplace.core.metadata.property.value;
    exports com.treilhes.jfxplace.core.metadata.property.value.effect;
    exports com.treilhes.jfxplace.core.metadata.property.value.effect.light;
    exports com.treilhes.jfxplace.core.metadata.property.value.keycombination;
    exports com.treilhes.jfxplace.core.metadata.property.value.list;
    exports com.treilhes.jfxplace.core.metadata.property.value.paint;
    exports com.treilhes.jfxplace.core.metadata.util;

    requires transitive jfxplace.fxom.model;
    requires transitive jfxplace.javafx.starter;
    requires transitive jfxplace.core.api;
    requires transitive emc4j.boot.starter;
    requires jfxplace.javafx.fxml.patch.link;
    requires emc4j.boot.api;


    provides Extension with FxomMetadataExtension;
}