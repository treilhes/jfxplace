package com.treilhes.jfxplace.test;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.api.FxToolkit;

import com.treilhes.jfxplace.test.builder.StageBuilder;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JfxPlaceExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private static final Logger logger = LoggerFactory.getLogger(JfxPlaceExtension.class);
    private static final Namespace EMC4J = create("com.treilhes.jfxplace");

    // This constructor is invoked by JUnit Jupiter via reflection or ServiceLoader
    @SuppressWarnings("unused")
    public JfxPlaceExtension() {
        // no-op
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        // no-op
    }


    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(final ExtensionContext context) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        var stage = FxToolkit.toolkitContext().getRegisteredStage();
        var scene = stage.getScene();
        if (scene != null && scene.getRoot() instanceof Pane r) {
            Platform.runLater(() -> {
                r.getChildren().clear();
            });
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        // Check if the parameter is supported, e.g., by type or annotation
        var type = parameterContext.getParameter().getType();
        return type == Stage.class || type == StageBuilder.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        var type = parameterContext.getParameter().getType();

        if (type == Stage.class) {
            return FxToolkit.toolkitContext().getRegisteredStage();
        }

        return null;
    }

}
