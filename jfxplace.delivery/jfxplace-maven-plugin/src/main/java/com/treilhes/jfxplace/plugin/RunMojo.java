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
package com.treilhes.jfxplace.plugin;

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

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3DomWriter;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;

import com.treilhes.emc4j.plugin.RunMojo.Registry;

@Mojo(name = "run", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class RunMojo extends AbstractMojo {

    protected static final String DEFAULT_EMC4J_VERSION = "1.0.0-SNAPSHOT";
    protected static final String DELIVERY_CONFIG_GROUP_ID = "com.treilhes.jfxplace";
    protected static final String DELIVERY_CONFIG_ARTIFACT_ID = "jfxplace.delivery.config";

    @Component
    private BuildPluginManager pluginManager;

    @Component
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession mavenSession;

    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;

    @Parameter(property = "emc4jVersion", required = false, defaultValue = DEFAULT_EMC4J_VERSION)
    private String emc4jVersion;

    @Parameter
    private Dependency emc4jRuntimeDependency;

    @Parameter
    private Dependency emc4jConfigurationDependency;

    @Parameter(property = "jfxplaceVersion", required = true)
    private String jfxplaceVersion;

    @Parameter
    private Dependency jfxplaceConfigurationDependency;

    @Parameter
    private List<Dependency> configurationDependencies = new ArrayList<>();

    @Parameter(property = "outputDirectory", required = false, defaultValue = "target/jfxplace-maven-plugin")
    private String outputDirectory;

    @Parameter
    private List<String> profiles = new ArrayList<>();

    @Parameter
    private List<File> profileFiles = new ArrayList<>();

    @Parameter(property = "javaOptions", required = false)
    private List<String> javaOptions;

    /**
     * The application identifier to run. The application identifier must be
     * contained in one of the provided registry dependencies.
     */
    @Parameter(property = "applicationId", required = true)
    private String applicationId;

    /**
     * If true, the artifacts deployed into the run directory will be deleted first,
     * before being copied. If false, only the configuration file will be updated,
     * and the dependencies will not be copied again.
     */
    @Parameter(property = "clean", defaultValue = "true")
    private boolean clean;

    /**
     * If true, the JVM will be started in debug mode, allowing a debugger to
     * attach. The default value is false.
     */
    @Parameter(property = "debug", defaultValue = "false")
    private boolean debug;
    /**
     * If true, the JVM will wait for a debugger to attach before starting
     * execution. The default value is true.
     */
    @Parameter(property = "debugSuspend", defaultValue = "true")
    private boolean debugSuspend;

    /**
     * The debug port to use when starting the JVM in debug mode. The default port
     * is 8000.
     */
    @Parameter(property = "debugPort", defaultValue = "8000")
    private int debugPort;

    @Parameter
    private Registry registry;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        profiles.add(0, "jfxplace");

        try {
            var emc4jRuntimeDependencyElement = dependencyToElement("emc4jRuntimeDependency", emc4jRuntimeDependency);

            // get the overrided jfxplace configuration
            var jfxplaceConfigurationDependencyElement = dependencyToElement("dependency", jfxplaceConfigurationDependency);

            // create the default one if not provided
            if (jfxplaceConfigurationDependencyElement == null) {
                jfxplaceConfigurationDependencyElement = element(
                        name("dependency"),
                        element(name("groupId"), DELIVERY_CONFIG_GROUP_ID),
                        element(name("artifactId"), DELIVERY_CONFIG_ARTIFACT_ID),
                        element(name("version"), jfxplaceVersion)
                );
            }

            // other configuration dependencies with the jfxplace one added first
            var configurationDependenciesElements = configurationDependencies.stream()
                    .map(dep -> dependencyToElement("dependency", dep))
                    .toList();

            var allConfigurationDependenciesElements = new ArrayList<Element>();
            allConfigurationDependenciesElements.add(jfxplaceConfigurationDependencyElement);
            allConfigurationDependenciesElements.addAll(configurationDependenciesElements);


            var javaOptionsEleemnts = javaOptions.stream().map(opt -> element(name("option"), opt)).toArray(Element[]::new);
            var profileElements = profiles.stream().map(p -> element(name("profile"), p)).toArray(Element[]::new);
            var profileFileElements = profileFiles.stream().map(f -> element(name("profileFile"), f.getAbsolutePath())).toArray(Element[]::new);

            var elementList = new ArrayList<Element>();

            elementList.addAll(List.of(
                    element(name("skip"), Boolean.toString(skip)),
                    element(name("emc4jVersion"), emc4jVersion),
                    element(name("configurationDependencies"), allConfigurationDependenciesElements.toArray(Element[]::new)),
                    element(name("outputDirectory"), outputDirectory),
                    element(name("profiles"), profileElements),
                    element(name("profileFiles"), profileFileElements),

                    element(name("javaOptions"), javaOptionsEleemnts),
                    element(name("applicationId"), applicationId),
                    element(name("clean"), Boolean.toString(clean)),
                    element(name("debug"), Boolean.toString(debug)),
                    element(name("debugSuspend"), Boolean.toString(debugSuspend)),
                    element(name("debugPort"), Integer.toString(debugPort))
            ));

            if (emc4jRuntimeDependencyElement != null) {
                elementList.add(jfxplaceConfigurationDependencyElement);
            }

            if (registry != null) {
                var registryElements = new ArrayList<Element>();
                registryElements.add(element(name("groupId"), registry.getGroupId()));
                registryElements.add(element(name("artifactId"), registry.getArtifactId()));
                registryElements.add(element(name("version"), registry.getVersion()));
                if (registry.getLocalFolder() != null) {
                    registryElements.add(element(name("localFolder"), registry.getLocalFolder().getPath()));
                }
                var registryElement = element(name("registry"), registryElements.toArray(Element[]::new));
                elementList.add(registryElement);
            }

            var configuration = configuration(elementList.toArray(Element[]::new));


            StringWriter writer = new StringWriter();
            Xpp3DomWriter.write(writer, configuration);

            getLog().info("EMC4J run configuration:\n" + writer.toString());

            executeMojo(
                    plugin(
                        groupId("com.treilhes.emc4j"),
                        artifactId("emc4j-maven-plugin"),
                        version(emc4jVersion)
                    ),
                    goal("run"),
                    configuration,
                    executionEnvironment(
                        project,
                        mavenSession,
                        pluginManager
                    )
            );

        } catch (Exception e) {
            getLog().error("Error executing run goal", e);
            throw new MojoExecutionException("Error executing run goal", e);
        }
        System.out.println("Run executed");
    }

    private Element dependencyToElement(String name, Dependency dependency) {
        return dependency != null
                ? element(name(name), element(name("groupId"), dependency.getGroupId()),
                        element(name("artifactId"), dependency.getArtifactId()),
                        element(name("version"), dependency.getVersion()))
                : null;

    }

}
