package com.gluonhq.jfxapps.core.api.application;

import com.treilhes.emc4j.boot.api.loader.OpenCommandEvent;

public interface OpenCommandEventHandler {
	void handleOpenCommand(OpenCommandEvent command);
}
