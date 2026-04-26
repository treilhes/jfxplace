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
package com.treilhes.jfxplace.core.fxom.util;

import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.FXOMInstance;
import com.treilhes.jfxplace.core.fxom.FXOMIntrinsic;
import com.treilhes.jfxplace.core.fxom.FXOMNode;
import com.treilhes.jfxplace.core.fxom.FXOMNodes;
import com.treilhes.jfxplace.core.fxom.FXOMProperty;
import com.treilhes.jfxplace.core.fxom.FXOMPropertyC;
import com.treilhes.jfxplace.core.fxom.FXOMPropertyT;

import javafx.scene.control.ToggleGroup;

public class ToggleGroupHelper {

    public static final PropertyName toggleGroupName = new PropertyName("toggleGroup");

    private ToggleGroupHelper() {
        // static helper class
    }

    public static boolean isToggleGroupReference(FXOMNode node) {

        if (FXOMNodes.extractReferenceSource(node) == null) {
            return false;
        }

        if (node instanceof FXOMIntrinsic intrinsic) {
            final FXOMProperty parentProperty = intrinsic.getParentProperty();

            if (parentProperty == null) {
                return false;
            }

            return parentProperty.getName().equals(toggleGroupName);
        }

        if (node instanceof FXOMPropertyT property) {
            return property.getName().equals(toggleGroupName);
        }

        return false;
    }


    public static FXOMPropertyC makeToggleGroup(FXOMDocument fxomDocument, String fxId) {
        final FXOMInstance toggleGroup = new FXOMInstance(fxomDocument, ToggleGroup.class);
        toggleGroup.setFxId(fxId);
        return new FXOMPropertyC(fxomDocument, toggleGroupName, toggleGroup);
    }
}
