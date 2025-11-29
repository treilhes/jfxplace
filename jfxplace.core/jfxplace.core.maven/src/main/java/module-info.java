open module jfxplace.core.maven {
    exports com.gluonhq.jfxapps.core.maven.artifact;
    exports com.gluonhq.jfxapps.core.maven;
    exports com.gluonhq.jfxapps.core.maven.impl;
    exports com.gluonhq.jfxapps.core.maven.preference;
    exports com.gluonhq.jfxapps.core.maven.repository;

    requires transitive jfxplace.core.api;
    requires jakarta.annotation;
    requires emc4j.boot.maven;

}