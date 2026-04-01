/*
 * Copyright (c) 2021, 2026, Pascal Treilhes and/or its affiliates.
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.treilhes.jfxplace.metadata.plugin.fork.Forker;
import com.treilhes.jfxplace.metadata.util.FxThreadinitializer;

@Mojo(name = "metadataResource", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class MetadataResourceMojo extends JfxAppsAbstractMojo {

    static {
        FxThreadinitializer.ENABLE_EXPERIMENTAL_FEATURES = false;
    }
    /**
     * Default constructor.
     */
    public MetadataResourceMojo() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {

        try {

            // Instantiate and populate MetadataSourceParams with plugin parameters
            MetadataResourceParams metadataResourceParams = new MetadataResourceParams();
            populateCommonValues(metadataResourceParams);

            Forker forker = new Forker(getLog());
            int result = forker.fork(project, pluginArtifacts, "com.treilhes.jfxplace.metadata.plugin.MetadataResourceProcess",
                    metadataResourceParams);

            if (result != 0) {
                throw new MojoExecutionException("Failed to complete the generating process! Forked process exited with code: " + result);
            }

        } catch (Exception e) {
            getLog().error("Failed to complete the generating process! " + e.getMessage(), e);
            throw new MojoExecutionException("Failed to complete the generating process!", e);
        }

    }


}