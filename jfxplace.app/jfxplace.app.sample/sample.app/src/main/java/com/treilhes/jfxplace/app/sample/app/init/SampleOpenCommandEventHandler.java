package com.treilhes.jfxplace.app.sample.app.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;
import com.treilhes.emc4j.boot.api.loader.RestartCommandEvent;
import com.treilhes.emc4j.boot.api.loader.RestartedCommandEvent;
import com.treilhes.emc4j.boot.api.loader.StopCommandEvent;
import com.treilhes.jfxplace.core.api.application.ApplicationActionFactory;
import com.treilhes.jfxplace.core.api.application.ApplicationInstance;
import com.treilhes.jfxplace.core.api.application.CommandEventHandler;
import com.treilhes.jfxplace.core.api.ui.dialog.ApplicationDialog;

@ApplicationSingleton
public class SampleOpenCommandEventHandler implements CommandEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(SampleOpenCommandEventHandler.class);

    private final ApplicationActionFactory applicationActionFactory;
    private final ApplicationDialog applicationDialog;

    public SampleOpenCommandEventHandler(ApplicationActionFactory applicationActionFactory,
            ApplicationDialog applicationDialog) {
        this.applicationActionFactory = applicationActionFactory;
        this.applicationDialog = applicationDialog;
    }

    @Override
    public void handleOpenCommand(OpenCommandEvent command) {
        try {
            applicationActionFactory.lookupUnusedInstance(null, this::open).perform();
        } catch (Exception e) {
            logger.error("Error while executing command", e);
            applicationDialog.addError("Error while executing command", e.getMessage(), e);
        }

    }

    private void open(ApplicationInstance instance) {
        instance.openWindow();
    }

    @Override
    public void handleStopCommand(StopCommandEvent command) {
        // not implemented, do nothing
    }

    @Override
    public void handleRestartCommand(RestartCommandEvent command) {
        // not implemented, do nothing
    }

    @Override
    public void handleRestartedCommand(RestartedCommandEvent command) {
        // not implemented, do nothing
    }

}
