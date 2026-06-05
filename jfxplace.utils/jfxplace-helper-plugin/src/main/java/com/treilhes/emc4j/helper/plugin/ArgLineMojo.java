/*
 * Copyright (c) 2021, 2025, Pascal Treilhes and/or its affiliates.
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
 *  - Neither the name of Pascal Treilhes nor the names of its
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
package com.treilhes.emc4j.helper.plugin;

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3DomWriter;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;
/**
 * Same as
 * https://maven.apache.org/plugins/maven-dependency-plugin/properties-mojo.html
 * Goal that sets a property pointing to the artifact file for each project
 * dependency. For each dependency (direct and transitive) a project property
 * will be set which follows the groupId:artifactId:type:[classifier] form and
 * contains the path to the resolved artifact.
 *
 * What this plugin does on top of the maven-dependency-plugin is to set the
 * properties for the plugin dependencies as well.
 * It allows to add properties for artifacts outside of the project classpath
 * For modular projects, this is useful to set the properties for the patched artifacts
 */
@Mojo(name = "argline", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.INITIALIZE, threadSafe = true)
public class ArgLineMojo extends AbstractMojo {

    private static final String DEFAULT_EMC4J_VERSION = "1.0.0-SNAPSHOT";

    //@formatter:off
    private static final Map<String, String> patches = Map.of(
        "javafx.fxml","com.treilhes.jfxplace:javafx.fxml.patch",
        "javafx.graphics", "com.treilhes.jfxplace:javafx.graphics.patch"
    );
    //@formatter:on

    @Component
    private BuildPluginManager pluginManager;

    @Component
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    @Parameter(defaultValue = "${mojoExecution}",
            readonly = true,
            required = true)
    private MojoExecution mojoExecution;

    @Parameter
    private Map<String, String> extraPatches;

    @Parameter(defaultValue = "argLine")
    private String propertyName;

    @Parameter(property = "emc4jVersion", required = false, defaultValue = DEFAULT_EMC4J_VERSION)
    private String emc4jVersion;

    public ArgLineMojo() {
        // Default constructor
    }

    @Override
    public void execute() throws MojoExecutionException {
        var version = mojoExecution.getPlugin().getVersion();

        try {
            var elements = new ArrayList<Element>();
            var extraPatchesList = new ArrayList<Element>();

            for (var patch : patches.entrySet()) {
                var module = patch.getKey();
                var artifactCoords = patch.getValue() + ":" + version;
                extraPatchesList.add(element(name(module), artifactCoords));
            }

            if (extraPatches != null) {
                for (var extraPatch : extraPatches.entrySet()) {
                    var module = extraPatch.getKey();
                    var artifactCoords = extraPatch.getValue();
                    extraPatchesList.add(element(name(module), artifactCoords));
                }
            }

            elements.add(element(name("extraPatches"), extraPatchesList.toArray(Element[]::new)));
            elements.add(element(name("propertyName"), propertyName));

            var configuration = configuration(elements.toArray(Element[]::new));

            StringWriter writer = new StringWriter();
            Xpp3DomWriter.write(writer, configuration);

            getLog().info("JFXPLACE run configuration:\n" + writer.toString());

            executeMojo(
                    plugin(
                        groupId("com.treilhes.emc4j"),
                        artifactId("emc4j-helper-plugin"),
                        version(emc4jVersion)
                    ),
                    goal("argline"),
                    configuration,
                    executionEnvironment(
                        project,
                        session,
                        pluginManager
                    )
            );

        } catch (Exception e) {
            throw new MojoExecutionException("Error while resolving patch artifacts", e);
        }
    }

}