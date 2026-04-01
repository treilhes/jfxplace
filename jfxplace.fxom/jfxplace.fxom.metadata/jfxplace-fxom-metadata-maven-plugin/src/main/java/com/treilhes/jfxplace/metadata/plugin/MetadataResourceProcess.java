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
package com.treilhes.jfxplace.metadata.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treilhes.jfxplace.metadata.finder.api.Descriptor;
import com.treilhes.jfxplace.metadata.finder.api.IClassCrawler;
import com.treilhes.jfxplace.metadata.finder.api.SearchContext;
import com.treilhes.jfxplace.metadata.finder.impl.ClassCrawler;
import com.treilhes.jfxplace.metadata.finder.impl.DescriptorCollector;
import com.treilhes.jfxplace.metadata.finder.impl.JarFinder;
import com.treilhes.jfxplace.metadata.finder.impl.MatchingJarCollector;
import com.treilhes.jfxplace.metadata.properties.api.PropertyGenerationContext;
import com.treilhes.jfxplace.metadata.properties.api.PropertyGenerator;
import com.treilhes.jfxplace.metadata.properties.impl.PropertyGeneratorImpl;
import com.treilhes.jfxplace.metadata.util.FxThreadinitializer;
import com.treilhes.jfxplace.metadata.util.Report;

import javafx.application.Platform;

public class MetadataResourceProcess extends JfxAppsAbstractProcess {

    static {
        FxThreadinitializer.ENABLE_EXPERIMENTAL_FEATURES = false;
    }
    /**
     * Default constructor.
     */
    public MetadataResourceProcess() {
        super();
    }

    public static void main(String[] args) throws IOException {
        MetadataResourceProcess process = new MetadataResourceProcess();

        File paramsFile = new File(args[0]);
        ObjectMapper mapper = new ObjectMapper();
        MetadataResourceParams params = mapper.readerFor(MetadataResourceParams.class).readValue(paramsFile);

        try {
            process.execute(params);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void execute(MetadataResourceParams params) throws MetadataGenerationException {
        Report.enableGlobalReport = params.isGlobalReportEanbled();

        try {

            if (!FxThreadinitializer.initJFX(params.getJavafxVersion())) {
                throw new MetadataGenerationException("Failed to initialize JavaFX thread");
            }

            var classloader = this.getClass().getClassLoader();
            final SearchContext searchContext = createSearchContext(classloader, params);
            final PropertyGenerationContext propertyContext = createPropertyGenerationContext(classloader, params);

            DescriptorCollector descriptorCollector = new DescriptorCollector();
            MatchingJarCollector jarCollector = new MatchingJarCollector(searchContext.getJarFilterPatterns());

            JarFinder.listJarsInClasspath(params.getJarFiles(), List.of(jarCollector, descriptorCollector));
            Set<Path> jars = jarCollector.getCollected();
            Set<Descriptor> descriptors = descriptorCollector.getCollected();

            IClassCrawler crawler = new ClassCrawler();
            PropertyGenerator generator = new PropertyGeneratorImpl(propertyContext);

            final CompletableFuture<Boolean> returnValue = new CompletableFuture<>();

            Runnable runnable = () -> {
                try {
                    var classes = crawler.crawl(jars, searchContext);
                    generator.generateProperties(classes, descriptors);
                    returnValue.complete(true);
                } catch (Exception e) {
                    returnValue.completeExceptionally(e);
                }
            };

            Platform.runLater(runnable);

            if (returnValue.get()) {
                System.out.println("Generating process completed successfully.");
            }

            if (returnValue.isCompletedExceptionally()) {
                throw new MetadataGenerationException("Failed to complete the generating process!", returnValue.exceptionNow());
            }

        } catch (Exception e) {
            throw new MetadataGenerationException("Failed to complete the generating process!", e);
        } finally {
            FxThreadinitializer.stop();
        }

    }

}
