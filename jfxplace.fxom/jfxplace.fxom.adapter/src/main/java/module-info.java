module jfxplace.fxom.adapter {
    exports com.treilhes.jfxplace.adapter.appmngr.action;

    requires transitive emc4j.boot.api;
    requires transitive jfxplace.core.api;
    requires jfxplace.fxom.api;
}