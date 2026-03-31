package com.treilhes.jfxplace.core.api.fs;

import java.io.File;

public interface OpenFileHandler {
	boolean canOpen(File file);
	void open(File file);
}
