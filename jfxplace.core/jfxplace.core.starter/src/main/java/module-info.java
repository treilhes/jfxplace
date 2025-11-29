module jfxplace.core.starter {

    requires transitive jfxplace.javafx.starter;

    requires transitive emc4j.boot.starter;

    requires transitive emc4j.boot.api;

    requires transitive org.slf4j;
    requires transitive io.reactivex.rxjava3;
    requires transitive org.reactivestreams;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive org.pdfsam.rxjavafx;

    requires transitive jakarta.annotation;
    requires transitive jakarta.persistence;
    requires transitive jakarta.validation;

    requires transitive org.aspectj.weaver;

    requires transitive org.hibernate.validator;

    requires transitive java.prefs;


}