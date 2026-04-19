package com.treilhes.jfxplace.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;
import org.testfx.framework.junit5.ApplicationExtension;

import com.treilhes.emc4j.test.Emc4jCoreContext;
import com.treilhes.emc4j.test.Emc4jDefault;
import com.treilhes.emc4j.test.Emc4jExtension;
import com.treilhes.emc4j.test.Emc4jExtension.Emc4jTestContextBootstrapper;
import com.treilhes.emc4j.test.Emc4jSpringExtension;
import com.treilhes.emc4j.test.Emc4jTest;
import com.treilhes.jfxplace.core.api.application.ApplicationClassloader;
import com.treilhes.jfxplace.core.api.fxom.subjects.FxomEvents;
import com.treilhes.jfxplace.core.api.i18n.BundleProvider;
import com.treilhes.jfxplace.core.api.i18n.I18N;
import com.treilhes.jfxplace.core.api.javafx.internal.FxmlControllerBeanPostProcessor;
import com.treilhes.jfxplace.core.api.javafx.internal.JfxAppPlatformImpl;
import com.treilhes.jfxplace.core.api.subjects.ApplicationEvents;
import com.treilhes.jfxplace.core.api.subjects.ApplicationInstanceEvents;
import com.treilhes.jfxplace.core.api.subjects.DockManager;
import com.treilhes.jfxplace.core.api.subjects.LifecyclePostProcessor;
import com.treilhes.jfxplace.core.api.subjects.ViewManager;
import com.treilhes.jfxplace.core.api.task.TaskService;
import com.treilhes.jfxplace.core.api.ui.controller.dock.ViewController;
import com.treilhes.jfxplace.test.JfxPlaceTest.I18NTestConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@BootstrapWith(Emc4jTestContextBootstrapper.class)
@ExtendWith({
    ApplicationExtension.class,
    MockitoExtension.class,
    Emc4jSpringExtension.class,
    Emc4jExtension.class
})
@Emc4jTest(
        defaultConfig = @Emc4jDefault(
                classes = {
                        I18NTestConfig.class,
                        JfxAppPlatformImpl.class,
                        LifecyclePostProcessor.class,

                        // events
                        ApplicationEvents.ApplicationEventsImpl.class,
                        ApplicationInstanceEvents.ApplicationInstanceEventsImpl.class,
                        FxomEvents.FxomEventsImpl.class,
                        ViewManager.ViewManagerImpl.class,
                        DockManager.DockManagerImpl.class,

                        // JavaFX
                        ApplicationClassloader.class,
                        FxmlControllerBeanPostProcessor.class,
                        //FxmlControllerBeanPostProcessor2.class,

                        // services
                        TaskService.class,

                        //UI
                        ViewController.class, //base ui for views
                }
        )
)
public @interface JfxPlaceTest {

    @AliasFor(annotation = Emc4jTest.class)
    String[] properties() default {};

    @AliasFor(annotation = Emc4jTest.class)
    WebEnvironment webEnvironment() default WebEnvironment.NONE;

    @AliasFor(annotation = Emc4jTest.class)
    boolean enableJpa() default false;

    @AliasFor(annotation = Emc4jTest.class)
    boolean enableAop() default false;

    @AliasFor(annotation = Emc4jTest.class)
    boolean loadDefaultScopes() default true;

    @AliasFor(annotation = Emc4jTest.class)
    Class<?>[] classes() default {};

    @AliasFor(annotation = Emc4jTest.class)
    Emc4jCoreContext context() default @Emc4jCoreContext;

    //sEmc4jDefault defaultConfig() default @Emc4jDefault;

    @TestConfiguration
    static class I18NTestConfig {
        @Bean("i18n")
        @ConditionalOnMissingBean
        I18N i18nTest(List<BundleProvider> bundleProviders) {
            return new I18N(bundleProviders, true);
        }
    }
}
