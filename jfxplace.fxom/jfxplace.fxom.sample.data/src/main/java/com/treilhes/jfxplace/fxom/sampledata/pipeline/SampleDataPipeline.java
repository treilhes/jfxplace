package com.treilhes.jfxplace.fxom.sampledata.pipeline;

import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.NonNull;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPostDeserialization;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPreSerialization;
import com.treilhes.jfxplace.core.fxom.sample.SampleDataEnabledPreference;

@ApplicationInstanceSingleton
public class SampleDataPipeline implements FXOMPostDeserialization, FXOMPreSerialization {

    private final Optional<SampleDataGenerator> sampleDataGenerator;
    private final SampleDataEnabledPreference sampleDataEnabledPreference;

    public SampleDataPipeline(
            SampleDataEnabledPreference sampleDataEnabledPreference,
            Optional<SampleDataGenerator> sampleDataGenerator) {
        this.sampleDataEnabledPreference = sampleDataEnabledPreference;
        this.sampleDataGenerator = sampleDataGenerator;
    }

    @Override
    public FXOMDocument preSerializationTransform(FXOMDocument document, @NonNull Map<Class<?>, Object> processContext) {
        if (Boolean.TRUE.equals(sampleDataEnabledPreference.getValue())) {
            sampleDataGenerator.ifPresent(generator -> generator.removeSampleData(document.getFxomRoot()));
        }
        return document;
    }

    @Override
    public FXOMDocument postDeserializationTransform(FXOMDocument document, @NonNull Map<Class<?>, Object> processContext) {
        if (Boolean.TRUE.equals(sampleDataEnabledPreference.getValue())) {
            sampleDataGenerator.ifPresent(generator -> generator.assignSampleData(document.getFxomRoot()));
        }
        return document;
    }


}
