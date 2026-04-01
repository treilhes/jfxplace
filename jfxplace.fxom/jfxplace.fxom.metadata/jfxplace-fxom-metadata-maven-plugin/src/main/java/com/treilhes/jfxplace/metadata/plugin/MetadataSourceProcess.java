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
import com.treilhes.jfxplace.metadata.java.api.JavaGenerationContext;
import com.treilhes.jfxplace.metadata.java.impl.JavaGeneratorImpl;
import com.treilhes.jfxplace.metadata.properties.api.PropertyGenerationContext;
import com.treilhes.jfxplace.metadata.util.FxThreadinitializer;
import com.treilhes.jfxplace.metadata.util.Report;

import javafx.application.Platform;

public class MetadataSourceProcess extends JfxAppsAbstractProcess{

    static {
        FxThreadinitializer.ENABLE_EXPERIMENTAL_FEATURES = false;
    }
    /**
     * Default constructor.
     */
    public MetadataSourceProcess() {
        super();
    }


    public static void main(String[] args) throws IOException {
        MetadataSourceProcess process = new MetadataSourceProcess();

        File paramsFile = new File(args[0]);
        ObjectMapper mapper = new ObjectMapper();
        MetadataSourceParams params = mapper.readerFor(MetadataSourceParams.class).readValue(paramsFile);

        try {
            process.execute(params);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void execute(MetadataSourceParams params) throws MetadataGenerationException {
        Report.enableGlobalReport = params.isGlobalReportEanbled();

        try {

            if (!FxThreadinitializer.initJFX(params.getJavafxVersion())) {
                throw new MetadataGenerationException("Failed to initialize JavaFX thread");
            }

            final ClassLoader cp = this.getClass().getClassLoader();
            final SearchContext searchContext = createSearchContext(cp, params);
            final PropertyGenerationContext propertyContext = createPropertyGenerationContext(cp, params);
            final JavaGenerationContext javaContext = createJavaGenerationContext(params);

            DescriptorCollector descriptorCollector = new DescriptorCollector();
            MatchingJarCollector jarCollector = new MatchingJarCollector(searchContext.getJarFilterPatterns());

            JarFinder.listJarsInClasspath(params.getJarFiles(), List.of(jarCollector, descriptorCollector));
            Set<Path> jars = jarCollector.getCollected();
            Set<Descriptor> descriptors = descriptorCollector.getCollected();

            IClassCrawler crawler = new ClassCrawler();
            JavaGeneratorImpl generator = new JavaGeneratorImpl(propertyContext, javaContext);

            final CompletableFuture<Boolean> returnValue = new CompletableFuture<>();

            Runnable runnable = () -> {
                try {
                    var classes = crawler.crawl(jars, searchContext);
                    generator.generateJavaFiles(classes, descriptors);
                    returnValue.complete(true);
                } catch (Exception e) {
                    returnValue.completeExceptionally(e);
                }
            };

            Platform.runLater(runnable);

            if (returnValue.get()) {
                System.out.println("Generation process completed successfully!");
            }

            if (returnValue.isCompletedExceptionally()) {
                throw new MetadataGenerationException("Failed to complete the generating process!", returnValue.exceptionNow());
            }

            if (Report.flush(true)) {
                throw new MetadataGenerationException(
                        "Some errors occured during the generation process, please see the logs!");
            }
        } catch (Exception e) {
            System.err.println("Failed to complete the generation process! " + e.getMessage());
            Report.flush(true);
        } finally {
            FxThreadinitializer.stop();
        }

    }

    private JavaGenerationContext createJavaGenerationContext(MetadataSourceParams params) throws MetadataGenerationException {

        JavaGenerationContext javaContext = new JavaGenerationContext();

        if (params.getSourceFolder() != null && !params.getSourceFolder().exists()) {
            params.getSourceFolder().mkdirs();
        }
        javaContext.setSourceFolder(params.getSourceFolder());

        javaContext.setInputResourceFolder(params.getInputResourceFolder());

        javaContext.setTargetPackage(params.getTargetPackage());

        javaContext.setModuleName(params.getModuleName());

        for (String s : params.getModuleRequires()) {
            javaContext.addModuleRequire(s);
        }

        javaContext.setParentUuid(params.getParentUuid());
        javaContext.setUuid(params.getUuid());

        javaContext.setExtensionName(params.getExtensionName());

        javaContext.setMetadataPrefix(params.getMetadataPrefix());

        javaContext.setComponentCustomizationTemplate(params.getTemplateForComponentCustomization());
        javaContext.setComponentPropertyCustomizationTemplate(params.getTemplateForComponentPropertyCustomization());
        javaContext.setValuePropertyCustomizationTemplate(params.getTemplateForValuePropertyCustomization());
        javaContext.setStaticValuePropertyCustomizationTemplate(params.getTemplateForStaticValuePropertyCustomization());
        javaContext.setComponentConstructorCustomizationTemplate(params.getTemplateForComponentConstructorCustomization());


        javaContext.setTargetComponentSuperClass(params.getTargetComponentSuperClass());
        javaContext.setTargetComponentCustomizationClass(params.getTargetComponentCustomizationClass());
        javaContext.setTargetComponentPropertyCustomizationClass(params.getTargetComponentPropertyCustomizationClass());
        javaContext.setTargetValuePropertyCustomizationClass(params.getTargetValuePropertyCustomizationClass());
        return javaContext;
    }
}
