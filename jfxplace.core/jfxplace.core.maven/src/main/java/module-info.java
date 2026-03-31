open module jfxplace.core.maven {
    exports com.treilhes.jfxplace.core.maven.artifact;
    exports com.treilhes.jfxplace.core.maven;
    exports com.treilhes.jfxplace.core.maven.impl;
    exports com.treilhes.jfxplace.core.maven.preference;
    exports com.treilhes.jfxplace.core.maven.repository;

    requires transitive jfxplace.core.api;
    requires jakarta.annotation;
    requires emc4j.boot.maven;

}