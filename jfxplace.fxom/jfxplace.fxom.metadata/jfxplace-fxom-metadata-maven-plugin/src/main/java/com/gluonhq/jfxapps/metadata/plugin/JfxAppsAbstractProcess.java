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
package com.gluonhq.jfxapps.metadata.plugin;

import java.lang.reflect.Constructor;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.gluonhq.jfxapps.metadata.finder.api.SearchContext;
import com.gluonhq.jfxapps.metadata.plugin.params.ConstructorOverride;
import com.gluonhq.jfxapps.metadata.properties.api.PropertyGenerationContext;

public abstract class JfxAppsAbstractProcess {

    protected SearchContext createSearchContext(ClassLoader loader, MetadataParams params) throws MetadataGenerationException {

        SearchContext searchContext = new SearchContext();

        for (String s : params.getRootClasses()) {
            try {
                Class<?> cls = loader.loadClass(s);
                searchContext.addRootClass(cls);
            } catch (Exception e) {
                throw new MetadataGenerationException("Unable to load root class : " + s, e);
            }
        }

        for (String s : params.getExcludeClasses()) {
            try {
                Class<?> cls = loader.loadClass(s);
                searchContext.addExcludeClass(cls);
            } catch (Exception e) {
                throw new MetadataGenerationException("Unable to load excluded class : " + s, e);
            }
        }

        for (String s : params.getJarFilterPatterns()) {
            try {
                Pattern pattern = Pattern.compile(s);
                searchContext.addJarFilterPattern(pattern);
            } catch (Exception e) {
                throw new MetadataGenerationException("Unable to compile jar filter pattern : " + s, e);
            }
        }

        for (String s:params.getIncludePackages()) {
            searchContext.addIncludedPackage(s);
        }

        for (String s:params.getExcludePackages()) {
            searchContext.addExcludedPackage(s);
        }

        return searchContext;
    }

    protected PropertyGenerationContext createPropertyGenerationContext(ClassLoader loader, MetadataParams params) throws MetadataGenerationException {

        PropertyGenerationContext propertyContext = new PropertyGenerationContext(loader);
        try {
            if (params.getConstructorOverrides() != null) {
                for (ConstructorOverride cto : params.getConstructorOverrides()) {
                    Class<?> cls = Class.forName(cto.getCls());
                    Class<?>[] originalParameters = cto.getParameterOverrides().stream().map(p -> {
                        try {
                            return loader.loadClass(p.getCls());
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList()).toArray(new Class<?>[0]);

                    Class<?>[] newParameters = cto.getParameterOverrides().stream().map(p -> {
                        try {
                            return loader.loadClass(p.getOverridedBy());
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList()).toArray(new Class<?>[0]);

                    Constructor<?> constructor = cls.getConstructor(originalParameters);

                    propertyContext.addAltConstructor(constructor, newParameters);
                }
            }
        } catch (Exception e) {
            throw new MetadataGenerationException("Unable to override constructors", e);
        }

        if (params.getOutputResourceFolder() != null && !params.getOutputResourceFolder().exists()) {
            params.getOutputResourceFolder().mkdirs();
        }
        propertyContext.setOutputResourceFolder(params.getOutputResourceFolder());

        try {
            propertyContext.setComponentCustomizationClass(params.getComponentCustomizationClass());
        } catch (ClassNotFoundException e) {
            throw new MetadataGenerationException(e);
        }
        try {
            propertyContext.setComponentPropertyCustomizationClass(params.getComponentPropertyCustomizationClass());
        } catch (ClassNotFoundException e) {
            throw new MetadataGenerationException(e);
        }

        try {
            propertyContext.setValuePropertyCustomizationClass(params.getValuePropertyCustomizationClass());
        } catch (ClassNotFoundException e) {
            throw new MetadataGenerationException(e);
        }

        return propertyContext;
    }
}
