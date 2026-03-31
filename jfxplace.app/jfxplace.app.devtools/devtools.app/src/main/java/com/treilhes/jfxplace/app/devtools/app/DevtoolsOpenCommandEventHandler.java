package com.treilhes.jfxplace.app.devtools.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationSingleton;
import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;
import com.treilhes.emc4j.boot.api.loader.RestartCommandEvent;
import com.treilhes.emc4j.boot.api.loader.StopCommandEvent;
import com.treilhes.jfxplace.core.api.application.ApplicationActionFactory;
import com.treilhes.jfxplace.core.api.application.CommandEventHandler;
import com.treilhes.jfxplace.core.api.fs.OpenFileHandler;
import com.treilhes.jfxplace.core.api.ui.dialog.ApplicationDialog;

@ApplicationSingleton
public class DevtoolsOpenCommandEventHandler implements CommandEventHandler {

private static final Logger logger = LoggerFactory.getLogger(DevtoolsOpenCommandEventHandler.class);

	private final ApplicationActionFactory applicationActionFactory;
	private final ApplicationDialog applicationDialog;
	private final List<OpenFileHandler> openFileHandlers;

	public DevtoolsOpenCommandEventHandler(
			ApplicationActionFactory applicationActionFactory,
			ApplicationDialog applicationDialog,
			List<OpenFileHandler> openFileHandlers) {
		this.applicationActionFactory = applicationActionFactory;
		this.applicationDialog = applicationDialog;
		this.openFileHandlers = openFileHandlers;
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
