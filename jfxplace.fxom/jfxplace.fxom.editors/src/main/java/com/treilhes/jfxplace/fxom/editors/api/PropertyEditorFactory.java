package com.treilhes.jfxplace.fxom.editors.api;

import java.util.List;

import com.treilhes.jfxplace.core.metadata.property.PropertyMetadata;

public interface PropertyEditorFactory {

    PropertyEditor newEditor(PropertyMetadata propMeta);

    void releaseEditors(List<PropertyEditor> editorsInUse);

    void releaseEditor(PropertyEditor editor);

    PropertyEditorFactorySession newSession();

}