package com.gluonhq.jfxapps.metadata.plugin;

public class MetadataGenerationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MetadataGenerationException(String string, Throwable e) {
        super(string, e);
    }

    public MetadataGenerationException(String string) {
        super(string);
    }

    public MetadataGenerationException(Throwable e) {
        super(e);
    }

}
