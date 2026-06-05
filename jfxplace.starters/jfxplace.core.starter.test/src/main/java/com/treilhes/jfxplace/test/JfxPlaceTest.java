package com.treilhes.jfxplace.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;
import org.testfx.framework.junit5.ApplicationExtension;

import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.test.Emc4jCoreContext;
import com.treilhes.emc4j.test.Emc4jDefault;
import com.treilhes.emc4j.test.Emc4jExtension;
import com.treilhes.emc4j.test.Emc4jExtension.Emc4jTestContextBootstrapper;
import com.treilhes.emc4j.test.Emc4jSpringExtension;
import com.treilhes.emc4j.test.Emc4jTest;
import com.treilhes.jfxplace.core.api.application.Application;
import com.treilhes.jfxplace.core.api.application.ApplicationClassloader;
import com.treilhes.jfxplace.core.api.fxom.subjects.FxomEvents;
import com.treilhes.jfxplace.core.api.i18n.BundleProvider;
import com.treilhes.jfxplace.core.api.i18n.I18N;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstanceUi;
import com.treilhes.jfxplace.core.api.javafx.JfxPlaceExecutor;
import com.treilhes.jfxplace.core.api.javafx.internal.FxmlControllerBeanPostProcessor;
import com.treilhes.jfxplace.core.api.subjects.ApplicationEvents;
import com.treilhes.jfxplace.core.api.subjects.ApplicationInstanceEvents;
import com.treilhes.jfxplace.core.api.subjects.DockManager;
import com.treilhes.jfxplace.core.api.subjects.LifecyclePostProcessor;
import com.treilhes.jfxplace.core.api.subjects.ViewManager;
import com.treilhes.jfxplace.core.api.task.TaskService;
import com.treilhes.jfxplace.core.api.ui.controller.dock.ViewController;
import com.treilhes.jfxplace.core.api.ui.controller.misc.IconSetting;
import com.treilhes.jfxplace.test.JfxPlaceTest.JfxPlaceTestConfig;
import com.treilhes.jfxplace.test.builder.StageBuilder;

import javafx.stage.Stage;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@BootstrapWith(Emc4jTestContextBootstrapper.class)
@ExtendWith({
    ApplicationExtension.class,
    MockitoExtension.class,
    Emc4jSpringExtension.class,
    Emc4jExtension.class,
    JfxPlaceExtension.class
})
@Emc4jTest(
        defaultConfig = @Emc4jDefault(
                classes = {
                        JfxPlaceTestConfig.class,
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

                        // services
                        TaskService.class,

                        //UI
                        ViewController.class, //base ui for views

                        //Builder
                        StageBuilder.class
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
    static class JfxPlaceTestConfig {

        @Bean("i18n")
        @ConditionalOnMissingBean
        I18N i18nTest(List<BundleProvider> bundleProviders) {
            return new I18N(bundleProviders, true);
        }

        @Bean
        @ConditionalOnMissingBean
        Application application(EmContext context, ApplicationEvents events, I18N i18n, IconSetting iconSetting) {
            return new TestApplication(context, events, i18n, iconSetting);
        }

        @Bean
        @ConditionalOnMissingBean
        ApplicationInstance applicationInstance(Application application, EmContext context, ApplicationInstanceEvents events, ApplicationInstanceUi ui) {
            return new TestApplicationInstance(application, context, events, ui);
        }

        @Bean
        @ConditionalOnMissingBean
        ApplicationInstanceUi applicationInstanceUi() {
            return Mockito.mock(ApplicationInstanceUi.class);
        }

        @Bean
        @ConditionalOnMissingBean
        IconSetting iconSetting() {
            return Mockito.mock(IconSetting.class);
        }
    }

    static class TestApplication implements Application {

        private final EmContext context;
        private final ApplicationEvents events;
        private final JfxPlaceExecutor executor;
        private final I18N i18n;
        private final IconSetting iconSetting;

        public TestApplication(EmContext context, ApplicationEvents events, I18N i18n, IconSetting iconSetting) {
            super();
            this.context = context;
            this.events = events;
            this.i18n = i18n;
            this.iconSetting = iconSetting;
            this.executor = new JfxPlaceExecutor(context);
        }

        @Override
        public EmContext getContext() {
            return context;
        }

        @Override
        public ApplicationEvents getEvents() {
            return events;
        }

        @Override
        public JfxPlaceExecutor getExecutor() {
            return executor;
        }

        @Override
        public I18N getI18n() {
            return i18n;
        }

        @Override
        public IconSetting getIconSettings() {
            return iconSetting;
        }

        @Override
        public Stage newStage() {
            return new Stage();
        }
    }

    static class TestApplicationInstance implements ApplicationInstance {

        private final Application application;
        private final EmContext context;
        private final ApplicationInstanceEvents events;
        private final JfxPlaceExecutor executor;
        private final ApplicationInstanceUi ui;

        public TestApplicationInstance(Application application, EmContext context, ApplicationInstanceEvents events, ApplicationInstanceUi ui) {
            super();
            this.application = application;
            this.context = context;
            this.events = events;
            this.ui = ui;
            this.executor = new JfxPlaceExecutor(context);
        }

        @Override
        public EmContext getContext() {
            return context;
        }

        @Override
        public ApplicationInstanceEvents getEvents() {
            return events;
        }

        @Override
        public JfxPlaceExecutor getExecutor() {
            return executor;
        }

        @Override
        public ApplicationInstanceUi getUi() {
            return ui;
        }

        @Override
        public Application getApplication() {
            return application;
        }

        @Override
        public Stage newStage() {
            return new Stage();
        }

    }
}
