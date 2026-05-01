/*
 * Copyright (c) 2016, 2025, Gluon and/or its affiliates.
 * Copyright (c) 2021, 2025, Pascal Treilhes and/or its affiliates.
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
package com.treilhes.jfxplace.fxom.editors;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.treilhes.emc4j.boot.api.loader.extension.OpenExtension;
import com.treilhes.jfxplace.fxom.editors.base.ControllerClassEditor;
import com.treilhes.jfxplace.fxom.editors.base.CoreEditors;
import com.treilhes.jfxplace.fxom.editors.base.FxIdEditor;
import com.treilhes.jfxplace.fxom.editors.base.PropertyEditorFactoryImpl;
import com.treilhes.jfxplace.fxom.editors.control.BooleanEditor;
import com.treilhes.jfxplace.fxom.editors.control.BoundedDoubleEditor;
import com.treilhes.jfxplace.fxom.editors.control.ButtonTypeEditor;
import com.treilhes.jfxplace.fxom.editors.control.CharsetEditor;
import com.treilhes.jfxplace.fxom.editors.control.ColorPopupEditor;
import com.treilhes.jfxplace.fxom.editors.control.ColumnResizePolicyEditor;
import com.treilhes.jfxplace.fxom.editors.control.CursorEditor;
import com.treilhes.jfxplace.fxom.editors.control.DefaultEditors;
import com.treilhes.jfxplace.fxom.editors.control.DividerPositionsEditor;
import com.treilhes.jfxplace.fxom.editors.control.DoubleEditor;
import com.treilhes.jfxplace.fxom.editors.control.DurationEditor;
import com.treilhes.jfxplace.fxom.editors.control.EnumEditor;
import com.treilhes.jfxplace.fxom.editors.control.EventHandlerEditor;
import com.treilhes.jfxplace.fxom.editors.control.FunctionalInterfaceEditor;
import com.treilhes.jfxplace.fxom.editors.control.GenericEditor;
import com.treilhes.jfxplace.fxom.editors.control.I18nStringEditor;
import com.treilhes.jfxplace.fxom.editors.control.ImageEditor;
import com.treilhes.jfxplace.fxom.editors.control.IncludeFxmlEditor;
import com.treilhes.jfxplace.fxom.editors.control.InsetsEditor;
import com.treilhes.jfxplace.fxom.editors.control.IntegerEditor;
import com.treilhes.jfxplace.fxom.editors.control.NullableDoubleEditor;
import com.treilhes.jfxplace.fxom.editors.control.Point3DEditor;
import com.treilhes.jfxplace.fxom.editors.control.ResourceFileEditor;
import com.treilhes.jfxplace.fxom.editors.control.RotateEditor;
import com.treilhes.jfxplace.fxom.editors.control.StringEditor;
import com.treilhes.jfxplace.fxom.editors.control.StringListEditor;
import com.treilhes.jfxplace.fxom.editors.control.StyleClassEditor;
import com.treilhes.jfxplace.fxom.editors.control.StyleEditor;
import com.treilhes.jfxplace.fxom.editors.control.StylesheetEditor;
import com.treilhes.jfxplace.fxom.editors.control.TextAlignmentEditor;
import com.treilhes.jfxplace.fxom.editors.control.ToggleGroupEditor;
import com.treilhes.jfxplace.fxom.editors.i18n.I18NDefaultEditors;
import com.treilhes.jfxplace.fxom.editors.popupeditors.BoundsPopupEditor;
import com.treilhes.jfxplace.fxom.editors.popupeditors.EffectPopupEditor;
import com.treilhes.jfxplace.fxom.editors.popupeditors.FontPopupEditor;
import com.treilhes.jfxplace.fxom.editors.popupeditors.GenericPaintPopupEditor;
import com.treilhes.jfxplace.fxom.editors.popupeditors.KeyCombinationPopupEditor;
import com.treilhes.jfxplace.fxom.editors.popupeditors.Rectangle2DPopupEditor;
import com.treilhes.jfxplace.fxom.editors.popupeditors.StringPopupEditor;

public class FxomEditorsExtension implements OpenExtension {

    public static final UUID ID = UUID.fromString("94124a19-94ad-49b4-84a1-a621db188b6f");

    @Override
    public UUID getId() {
        return ID;
    }

    @Override
    public UUID getParentId() {
        return null;
    }

    @Override
    public List<Class<?>> exportedContextClasses() {
     // @formatter:off
        return Arrays.asList(
                ControllerClassEditor.class,
                CoreEditors.class,
                FxIdEditor.class,
                PropertyEditorFactoryImpl.class,

                I18NDefaultEditors.class,
                BooleanEditor.class,
                BoundedDoubleEditor.class,
                BoundsPopupEditor.class,
                ButtonTypeEditor.class,
                CharsetEditor.class,
                ColorPopupEditor.class,
                ColumnResizePolicyEditor.class,
                CursorEditor.class,
                DefaultEditors.class,
                DividerPositionsEditor.class,
                DoubleEditor.class,
                DurationEditor.class,
                EffectPopupEditor.class,
                EnumEditor.GenericEnumEditor.class,
                EventHandlerEditor.class,
                FontPopupEditor.class,
                FontPopupEditor.FontStyleEditor.class,
                FontPopupEditor.FontFamilyEditor.class,
                FontPopupEditor.FontStyleEditor.Factory.class,
                FontPopupEditor.FontFamilyEditor.Factory.class,
                FunctionalInterfaceEditor.class,
                GenericEditor.class,
                GenericPaintPopupEditor.class,
                I18nStringEditor.class,
                ImageEditor.class,
                IncludeFxmlEditor.class,
                InsetsEditor.class,
                IntegerEditor.class,
                KeyCombinationPopupEditor.class,
                NullableDoubleEditor.class,
                Point3DEditor.class,
                Rectangle2DPopupEditor.class,
                ResourceFileEditor.class,
                RotateEditor.class,
                StringEditor.class,
                StringListEditor.class,
                StringPopupEditor.class,
                StyleClassEditor.class,
                StyleEditor.class,
                StylesheetEditor.class,
                TextAlignmentEditor.class,
                ToggleGroupEditor.class
            );
     // @formatter:on
    }

    @Override
    public List<Class<?>> localContextClasses() {
        return List.of();
    }

}
