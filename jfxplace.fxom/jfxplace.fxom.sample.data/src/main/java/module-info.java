import com.treilhes.emc4j.boot.api.loader.extension.Extension;
import com.treilhes.jfxplace.fxom.sampledata.FxomSampleDataExtension;

module jfxplace.fxom.sample.data {
    exports com.treilhes.jfxplace.fxom.sampledata;
    exports com.treilhes.jfxplace.fxom.sampledata.data;

    requires transitive jfxplace.core.api;
    requires transitive jfxplace.fxom.api;

    provides Extension with FxomSampleDataExtension;
}