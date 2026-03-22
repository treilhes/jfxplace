package com.gluonhq.jfxapps.core.api.application;

import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;
import com.treilhes.emc4j.boot.api.loader.RestartCommandEvent;
import com.treilhes.emc4j.boot.api.loader.StopCommandEvent;

public interface CommandEventHandler {
	void handleOpenCommand(OpenCommandEvent command);
	void handleStopCommand(StopCommandEvent command);
	void handleRestartCommand(RestartCommandEvent command);
}
