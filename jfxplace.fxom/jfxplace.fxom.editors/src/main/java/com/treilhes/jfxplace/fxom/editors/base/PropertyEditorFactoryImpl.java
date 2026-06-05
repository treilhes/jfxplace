/*
 * Copyright (c) 2016, 2024, Gluon and/or its affiliates.
 * Copyright (c) 2021, 2024, Pascal Treilhes and/or its affiliates.
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation and Gluon nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.treilhes.jfxplace.fxom.editors.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;

import com.treilhes.emc4j.boot.api.context.EmContext;
import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.fxom.util.PropertyName;
import com.treilhes.jfxplace.core.metadata.property.PropertyMetadata;
import com.treilhes.jfxplace.core.metadata.property.ValuePropertyMetadata;
import com.treilhes.jfxplace.fxom.editors.api.EditorMapProvider;
import com.treilhes.jfxplace.fxom.editors.api.PropertyEditor;
import com.treilhes.jfxplace.fxom.editors.api.PropertyEditorFactory;
import com.treilhes.jfxplace.fxom.editors.api.PropertyEditorFactorySession;

@ApplicationInstanceSingleton
public class PropertyEditorFactoryImpl implements PropertyEditorFactory {

    // Map metadata class to editor class
    private final HashMap<Class<? extends PropertyMetadata>, List<Class<? extends PropertyEditor>>> metadataToEditors;

    // Map of editor pools
    private final HashMap<Class<? extends PropertyEditor>, Stack<PropertyEditor>> editorPools;

    /** The spring context. */
    private final EmContext context;

    public PropertyEditorFactoryImpl(
            EmContext context,
            List<EditorMapProvider> editorMapProviders
            ) {
        this.editorPools = new HashMap<>();
        this.metadataToEditors = new HashMap<>();
        this.context = context;
        populateMetadataToEditors(editorMapProviders);
    }

    private void populateMetadataToEditors(List<EditorMapProvider> editorMapProviders) {
        editorMapProviders.stream()
        .filter(Objects::nonNull)
        .map(EditorMapProvider::getMap)
        .filter(Objects::nonNull)
        .flatMap(map -> map.entrySet().stream())
        .forEach(e -> {
            var key = e.getKey();
            var value = e.getValue();
            var list = metadataToEditors.computeIfAbsent(key, _ -> new ArrayList<>());
            if (!list.contains(value)) {
                list.add(value);
            }
        });
    }

    /**
     * Find editor class from the provided mapping.
     *
     * @param valueClass the value class
     * @return the class$lt;? extends abstract editor>
     */
    private Class<? extends PropertyEditor> findEditorClass(Class<? extends PropertyMetadata> valueClass) {
        Class<?> uncheckedValueClass = valueClass;
        Class<? extends PropertyEditor> editorClass = null;

        while(editorClass == null && PropertyMetadata.class.isAssignableFrom(uncheckedValueClass)) {
            List<Class<? extends PropertyEditor>> possibleEditors = metadataToEditors.get(uncheckedValueClass);

            if (possibleEditors != null && possibleEditors.size() > 0) {
                //TODO handling multiple editors may be a good thing
                editorClass = possibleEditors.get(0);
            }

            if (editorClass != null) {
                return editorClass;
            }

            uncheckedValueClass = uncheckedValueClass.getSuperclass();
        }
        return null;
    }
    @Override
    public PropertyEditor newEditor(PropertyMetadata propMeta) {
        Class<? extends PropertyEditor> editorClass = findEditorClass(propMeta.getClass());

        if (editorClass == null) {
            return null;
        }

        if (!editorPools.containsKey(editorClass)) {
            editorPools.put(editorClass, new Stack<>());
        }

        Stack<PropertyEditor> editorPool = editorPools.get(editorClass);
        if ((editorPool != null) && !editorPool.isEmpty()) {
            return editorPool.pop();
        } else {
            return context.getBean(editorClass);
        }
    }

    @Override
    public void releaseEditors(List<PropertyEditor> editorsInUse) {
     // Put all the editors used in the editor pools
        for (PropertyEditor editor : editorsInUse) {
            releaseEditor(editor);
        }
    }
    @Override
    public void releaseEditor(PropertyEditor editor) {
        Stack<PropertyEditor> editorPool = editorPools.get(editor.getClass());
        assert editorPool != null;
        editorPool.push(editor);
        // remove all editor listeners
        editor.removeAllListeners();
    }

    public class PropertyEditorFactorySessionImpl implements PropertyEditorFactorySession {
        // Editors currently in use
        //   Could be a HashMap<SectionId, PropertyEditor>
        //   if we want to optimize a bit more the property editors usage,
        //   by re-using them directly in the GridPane, instead of using the pools.
        private final List<PropertyEditor> editorsInUse = new ArrayList<>();

        protected PropertyEditorFactorySessionImpl() {}

        @Override
        public PropertyEditor getEditor(ValuePropertyMetadata propMeta) {
            assert propMeta != null;

            PropertyEditor editor = newEditor(propMeta);

            assert editor != null;
            editor.setUpdateFromModel(true);
            editor.reset(propMeta);
            editor.setUpdateFromModel(false);
            editorsInUse.add(editor);
            return editor;
        }

        @Override
        public void clear() {
            releaseEditors(editorsInUse);
            editorsInUse.clear();
        }

        @Override
        public void reset(PropertyEditor... excludedEditors) {
            List<PropertyEditor> excluded = Arrays.asList(excludedEditors);
            editorsInUse.stream()
                .filter(e -> !excluded.contains(e))
                .forEach(e -> e.reset(e.getPropertyMeta()));
        }

        @Override
        public void forEach(Consumer<PropertyEditor> doSomething, PropertyEditor... excludedEditors) {
            List<PropertyEditor> excluded = Arrays.asList(excludedEditors);
            editorsInUse.stream()
                .filter(e -> !excluded.contains(e))
                .forEach(e -> doSomething.accept(e));
        }

        @Override
        public PropertyEditor getFxIdEditor() {
            return getEditor(CoreEditors.FXID_EDITOR);
        }

        @Override
        public PropertyEditor getControllerClassEditor() {
            return getEditor(CoreEditors.FXCONTROLLER_EDITOR);
        }

        @Override
        public PropertyEditor find(PropertyName propName) {
            try {
                return editorsInUse.stream().filter(e -> e.getPropertyName().equals(propName)).findFirst().get();
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    public PropertyEditorFactorySession newSession() {
        return new PropertyEditorFactorySessionImpl();
    }
}
