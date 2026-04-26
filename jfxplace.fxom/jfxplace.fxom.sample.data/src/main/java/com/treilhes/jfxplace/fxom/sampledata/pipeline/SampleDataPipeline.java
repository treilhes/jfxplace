package com.treilhes.jfxplace.fxom.sampledata.pipeline;

import java.util.Optional;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.fxom.FXOMDocument;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPostDeserialization;
import com.treilhes.jfxplace.core.fxom.pipeline.FXOMPreSerialization;
import com.treilhes.jfxplace.fxom.sampledata.preference.SampleDataEnabledPreference;

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
    public FXOMDocument preSerializationTransform(FXOMDocument document) {
        if (Boolean.TRUE.equals(sampleDataEnabledPreference.getValue())) {
            sampleDataGenerator.ifPresent(generator -> generator.removeSampleData(document.getFxomRoot()));
        }
        return document;
    }

    @Override
    public FXOMDocument postDeserializationTransform(FXOMDocument document) {
        if (Boolean.TRUE.equals(sampleDataEnabledPreference.getValue())) {
            sampleDataGenerator.ifPresent(generator -> generator.assignSampleData(document.getFxomRoot()));
        }
        return document;
    }


}
