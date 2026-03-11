package jfxapps.app.sample.app.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gluonhq.jfxapps.core.api.application.ApplicationActionFactory;
import com.gluonhq.jfxapps.core.api.application.OpenCommandEventHandler;
import com.gluonhq.jfxapps.core.api.ui.dialog.ApplicationDialog;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;

@ApplicationSingleton
public class SampleOpenCommandEventHandler implements OpenCommandEventHandler {

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

}
