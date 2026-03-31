package com.treilhes.jfxplace.app.sample.app.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.jfxplace.core.api.application.ApplicationActionFactory;
import com.treilhes.jfxplace.core.api.application.CommandEventHandler;
import com.treilhes.jfxplace.core.api.ui.dialog.ApplicationDialog;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;
import com.treilhes.emc4j.boot.api.loader.RestartCommandEvent;
import com.treilhes.emc4j.boot.api.loader.StopCommandEvent;

@ApplicationSingleton
public class SampleOpenCommandEventHandler implements CommandEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(SampleOpenCommandEventHandler.class);

    private final ApplicationActionFactory applicationActionFactory;
    private final ApplicationDialog applicationDialog;

    public SampleOpenCommandEventHandler(
            ApplicationActionFactory applicationActionFactory,
            ApplicationDialog applicationDialog) {
        this.applicationActionFactory = applicationActionFactory;
        this.applicationDialog = applicationDialog;
    }

    @Override
    public void handleOpenCommand(OpenCommandEvent command) {
        try {
            applicationActionFactory.lookupUnusedInstance(null, (instance) -> {
                instance.openWindow();
            }).perform();
        } catch (Exception e) {
            logger.error("Error while executing command", e);
            applicationDialog.addError("Error while executing command", e.getMessage(), e);
        }

    }

    @Override
    public void handleStopCommand(StopCommandEvent command) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleRestartCommand(RestartCommandEvent command) {
        // TODO Auto-generated method stub

    }

}
