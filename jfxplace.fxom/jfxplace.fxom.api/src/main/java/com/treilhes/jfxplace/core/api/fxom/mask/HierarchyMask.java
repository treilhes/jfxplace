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
package com.treilhes.jfxplace.core.api.fxom.mask;

import java.util.Collection;
import java.util.List;

import com.treilhes.jfxplace.core.fxom.FXOMObject;
import com.treilhes.jfxplace.core.fxom.util.PropertyName;
import com.treilhes.jfxplace.core.metadata.property.ValuePropertyMetadata;

public interface HierarchyMask<A extends Accessory> {
    boolean isAcceptingAccessory(A accessory);

    // FXOMObject getAccessory(Accessory accessory);

    A getAccessory(PropertyName accessoryName);

    List<FXOMObject> getAccessories(A accessory, boolean includeVirtuals);

    boolean hasMainAccessory();

    int getSubComponentCount(A accessory, boolean includeVirtuals);

    int getSubComponentCount(boolean includeVirtuals);

    FXOMObject getSubComponentAtIndex(A accessory, int i, boolean includeVirtuals);

    FXOMObject getSubComponentAtIndex(int i, boolean includeVirtuals);

    boolean isAcceptingAccessory(A accessory, FXOMObject newObject);

    FXOMObject getFxomObject();

    // new
    List<A> getAccessories();

    A getMainAccessory();

    FXOMObject getParentFXOMObject();

    boolean isResourceKey(PropertyName propertyNameForDescription);

    FXOMObject getClosestFxNode();

    PropertyName getPropertyNameForAccessory(A accessory);

    boolean isAcceptingSubComponent(FXOMObject newObject);

    boolean isAcceptingSubComponent(Collection<? extends FXOMObject> fxomObjects);

    List<FXOMObject> getSubComponents(boolean includeVirtuals);

    boolean isAcceptingAccessory(A targetAccessory, Collection<? extends FXOMObject> draggedObject);

    String getFxId();

    ValuePropertyMetadata getPropertyMetadata(PropertyName propertyName);

    Object getPropertyValue(PropertyName propertyName);

    Object getPropertySceneGraphValue(PropertyName propertyName);

    boolean isReadOnlyProperty(PropertyName propertyName);

    boolean isMultilineProperty(PropertyName propertyName);

    boolean hasProperty(PropertyName propertyName);

    /**
     * Returns the string value for this FXOM object node id property.
     *
     * @return
     */
    String getNodeId();

    A getAccessoryOf(FXOMObject childFxomObject);


}
