import com.treilhes.emc4j.boot.api.loader.extension.Extension;

import jfxapps.app.sample.app.SampleApiExtension;

open module sample.app {
    //exports jfxapps.app.sample.app;
    //exports jfxapps.app.sample.app.init;

    requires jfxplace.core.api;

    provides Extension with SampleApiExtension;
}