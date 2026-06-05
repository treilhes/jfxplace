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
package com.oracle.javafx.scenebuilder.app.editors;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import com.treilhes.jfxplace.core.api.i18n.I18N;
import com.treilhes.jfxplace.core.fxom.util.PropertyName;
import com.treilhes.jfxplace.core.metadata.property.PropertyGroupMetadata;
import com.treilhes.jfxplace.core.metadata.property.ValuePropertyMetadata;
import com.treilhes.jfxplace.core.metadata.property.value.IntegerPropertyMetadata;
import com.treilhes.jfxplace.core.metadata.property.value.StringPropertyMetadata;
import com.treilhes.jfxplace.core.metadata.property.value.StringPropertyMetadata.MultilineI18nStringPropertyMetadata;
import com.treilhes.jfxplace.fxom.editors.api.EditorContext;
import com.treilhes.jfxplace.fxom.editors.base.AutoSuggestEditor;

import javafx.beans.value.ObservableValue;

@ExtendWith({ ApplicationExtension.class, MockitoExtension.class })
class AutoSuggestEditorTest {

    private I18N i18n = new I18N(List.of(), true);

    @Mock
    EditorContext context;

    @BeforeEach
    void beforeEach() {
        when(context.i18n()).thenReturn(i18n);
    }

    @Test
    void shouldCreateAnEmptyInstance() {
        AutoSuggestEditorImpl o = new AutoSuggestEditorImpl(context);
        assertNotNull(o);
    }

    @Test
    void shouldCreateAnEmptyMenu() {
        AutoSuggestEditorImpl o = new AutoSuggestEditorImpl(context);
        assertNotNull(o.getMenu());
    }

    @Test
    void shouldResetTheInstance() {
        AutoSuggestEditorImpl o = new AutoSuggestEditorImpl(context);
        o.reset(someIntProp());
    }

    @Test
    void shouldResetTheInstanceForGroup() {
        AutoSuggestEditorImpl o = new AutoSuggestEditorImpl(context);
        o.reset(someGroupProp());
    }


    static IntegerPropertyMetadata someIntProp() {
        return new IntegerPropertyMetadata.Builder<Void>()
                .name(new PropertyName("int")) //NOCHECK
                .readWrite(true)
                .defaultValue(123).build();
    }
    static StringPropertyMetadata someStringProp() {
        return new MultilineI18nStringPropertyMetadata.Builder<Void>()
                .name(new PropertyName("string")) //NOCHECK
                .readWrite(true)
                .defaultValue("123").build();
    }
    static PropertyGroupMetadata someGroupProp() {
        return new PropertyGroupMetadata.Builder<Void>()
                .name(new PropertyName("group")) //NOCHECK
                .readWrite(true)
                .property("someInt", someIntProp())
                .property("someString", someStringProp()).build();
    }


    private class AutoSuggestEditorImpl extends AutoSuggestEditor {

        AutoSuggestEditorImpl(EditorContext context) {
            super(context);
        }

        @Override
        public void reset(ValuePropertyMetadata propMeta) {
            super.reset(propMeta);
        }

        @Override
        public ObservableValue<Boolean> focusedProperty() {
            return null;
        }

    }
}
