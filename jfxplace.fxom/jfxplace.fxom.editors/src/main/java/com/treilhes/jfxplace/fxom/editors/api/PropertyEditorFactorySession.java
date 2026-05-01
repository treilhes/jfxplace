package com.treilhes.jfxplace.fxom.editors.api;

import java.util.function.Consumer;

import com.treilhes.jfxplace.core.fxom.util.PropertyName;
import com.treilhes.jfxplace.core.metadata.property.ValuePropertyMetadata;

public interface PropertyEditorFactorySession {

    PropertyEditor getEditor(ValuePropertyMetadata propMeta);

    void clear();

    void reset(PropertyEditor... excludedEditors);

    void forEach(Consumer<PropertyEditor> doSomething, PropertyEditor... excludedEditors);

    PropertyEditor getFxIdEditor();

    PropertyEditor getControllerClassEditor();

    PropertyEditor find(PropertyName propName);

}