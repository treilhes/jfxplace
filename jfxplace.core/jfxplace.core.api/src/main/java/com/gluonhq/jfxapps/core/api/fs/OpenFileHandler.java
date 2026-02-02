package com.gluonhq.jfxapps.core.api.fs;

import java.io.File;

public interface OpenFileHandler {
	boolean canOpen(File file);
	void open(File file);
}
