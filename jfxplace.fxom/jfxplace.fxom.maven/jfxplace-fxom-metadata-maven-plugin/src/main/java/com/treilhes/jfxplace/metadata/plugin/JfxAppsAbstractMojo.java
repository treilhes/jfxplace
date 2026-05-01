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
package com.treilhes.jfxplace.metadata.plugin;

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.treilhes.jfxplace.metadata.plugin.params.ConstructorOverride;

public abstract class JfxAppsAbstractMojo extends AbstractMojo {

    @Parameter(property = "enableGlobalReport", required = false, alias = "enableGlobalReport")
    boolean enableGlobalReport = true;

    /** The input root classes. */
    @Parameter(property = "rootClasses", required = true, alias = "rootClasses")
    List<String> rootClasses;

    /** The input file. */
    @Parameter(property = "excludeClasses", required = false, alias = "excludeClasses")
    List<String> excludeClasses;

    /** The input file. */
    @Parameter(property = "jarFilterPatterns", required = false, alias = "jarFilterPatterns")
    List<String> jarFilterPatterns;

    /** The input root classes. */
    @Parameter(property = "includePackages", required = false)
    List<String> includePackages;

    /** The input file. */
    @Parameter(property = "excludePackages", required = false)
    List<String> excludePackages;

    @Parameter(property = "outputResourceFolder", required = false, defaultValue = "${project.build.directory}/generated-resources/jfxapps")
    File outputResourceFolder;

    @Parameter(property = "failOnError", required = false, defaultValue = "true")
    boolean failOnError = true;

    @Parameter(property = "javafxVersion", required = true)
    String javafxVersion;



    @Parameter(property = "constructorOverrides", required = false)
    List<ConstructorOverride> constructorOverrides;

    @Parameter(property = "componentCustomizationClass", required = false)
    String componentCustomizationClass;

    @Parameter(property = "componentPropertyCustomizationClass", required = false)
    String componentPropertyCustomizationClass;

    @Parameter(property = "valuePropertyCustomizationClass", required = false)
    String valuePropertyCustomizationClass;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Parameter(defaultValue = "${plugin.artifacts}", readonly = true)
    List<Artifact> pluginArtifacts;


    protected void populateCommonValues(MetadataParams metadataResourceParams) throws DependencyResolutionRequiredException {
        metadataResourceParams.setRootClasses(rootClasses);
        metadataResourceParams.setExcludeClasses(excludeClasses);
        metadataResourceParams.setJarFilterPatterns(jarFilterPatterns);
        metadataResourceParams.setIncludePackages(includePackages);
        metadataResourceParams.setExcludePackages(excludePackages);
        metadataResourceParams.setOutputResourceFolder(outputResourceFolder);
        metadataResourceParams.setJavafxVersion(javafxVersion);
        metadataResourceParams.setConstructorOverrides(constructorOverrides);
        metadataResourceParams.setComponentCustomizationClass(componentCustomizationClass);
        metadataResourceParams.setComponentPropertyCustomizationClass(componentPropertyCustomizationClass);
        metadataResourceParams.setValuePropertyCustomizationClass(valuePropertyCustomizationClass);

        var runtimeClasspathElements = project.getRuntimeClasspathElements().stream().map(File::new).toList();
        PluginDescriptor pluginDescriptor = (PluginDescriptor) getPluginContext().get("pluginDescriptor");
        List<File> cp = pluginDescriptor.getArtifacts().stream().map(a -> a.getFile()).toList();

        metadataResourceParams.getJarFiles().addAll(runtimeClasspathElements);
        metadataResourceParams.getJarFiles().addAll(cp);
    }
}
