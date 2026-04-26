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

package com.treilhes.jfxplace.fxom.sampledata.pipeline;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.fxom.FXOMCollection;
import com.treilhes.jfxplace.core.fxom.FXOMInstance;
import com.treilhes.jfxplace.core.fxom.FXOMObject;
import com.treilhes.jfxplace.core.fxom.FXOMProperty;
import com.treilhes.jfxplace.core.fxom.FXOMPropertyC;
import com.treilhes.jfxplace.fxom.sampledata.api.SampleData;

import javafx.scene.Node;

/**
 *
 */
@ApplicationInstanceSingleton
public class SampleDataGenerator {

    private final Map<String, SampleData> sampleDatas;

    public SampleDataGenerator(Optional<List<SampleData>> sampleDatas) {
        this.sampleDatas = sampleDatas.orElse(List.of()).stream()
                .collect(Collectors.toMap(s -> s.getClass().getName(), s -> s));
    }

    public void assignSampleData(FXOMObject startObject) {
        Objects.requireNonNull(startObject);

        final var sceneGraphObject = startObject.getSceneGraphObject().getAs(Node.class);

        if (sceneGraphObject != null && !sceneGraphObject.getProperties().containsKey(SampleData.SAMPLE_DATA_KEY)) {
            for (SampleData sampleData : sampleDatas.values()) {
                if (sampleData.canApplyTo(sceneGraphObject)) {
                    sampleData.applyTo(sceneGraphObject);
                    sceneGraphObject.getProperties().put(SampleData.SAMPLE_DATA_KEY, sampleData.getClass().getName());
                    break;
                }
            }
        }

        if (startObject instanceof FXOMInstance fxomInstance) {
            assignInstance(fxomInstance);
        } else if (startObject instanceof FXOMCollection fxomCollection) {
            assignCollection(fxomCollection);
        }
    }

    public void removeSampleData(FXOMObject startObject) {
        final Node sceneGraphObject = startObject.getSceneGraphObject().getAs(Node.class);

        if (sceneGraphObject == null) {
            return;
        }

        final var currentData = sceneGraphObject.getProperties().get(SampleData.SAMPLE_DATA_KEY);

        if (currentData != null) {
            var sampleData = sampleDatas.get(currentData.toString());
            if (sampleData != null) {
                sampleData.removeFrom(sceneGraphObject);
            }
        }

        if (startObject instanceof FXOMInstance fxomInstance) {
            cleanInstance(fxomInstance);
        } else if (startObject instanceof FXOMCollection fxomCollection) {
            cleanCollection(fxomCollection);
        }
    }

    private void assignCollection(FXOMCollection fxomCollection) {
        for (FXOMObject i : fxomCollection.getItems()) {
            assignSampleData(i);
        }
    }

    private void assignInstance(FXOMInstance fxomInstance) {
        for (FXOMProperty p : fxomInstance.getProperties().values()) {
            if (p instanceof FXOMPropertyC pc) {
                for (FXOMObject v : pc.getChildren()) {
                    assignSampleData(v);
                }
            }
        }
    }

    private void cleanCollection(FXOMCollection fxomCollection) {
        for (FXOMObject i : fxomCollection.getItems()) {
            removeSampleData(i);
        }
    }

    private void cleanInstance(FXOMInstance fxomInstance) {
        for (FXOMProperty p : fxomInstance.getProperties().values()) {
            if (p instanceof FXOMPropertyC pc) {
                for (FXOMObject v : pc.getChildren()) {
                    removeSampleData(v);
                }
            }
        }
    }

}
