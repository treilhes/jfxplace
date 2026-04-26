package com.treilhes.jfxplace.fxom.sampledata.preference;

import com.treilhes.emc4j.boot.api.context.annotation.ApplicationInstanceSingleton;
import com.treilhes.jfxplace.core.api.preference.DefaultValueProvider;
import com.treilhes.jfxplace.core.api.preference.Preference;
import com.treilhes.jfxplace.core.api.preference.PreferenceContext;


@ApplicationInstanceSingleton
@PreferenceContext(id = "e97dc950-9a4b-4cf8-9c65-143b0dec7ecb", // NO CHECK
        name = SampleDataEnabledPreference.PREFERENCE_KEY,
        defaultValueProvider = SampleDataEnabledPreference.DefaultProvider.class)
public interface SampleDataEnabledPreference extends Preference<Boolean> {

    public static final String PREFERENCE_KEY = "prefs.sample.data.enabled"; // NOCHECK
    public static final boolean PREFERENCE_DEFAULT_VALUE = false;

    public static class DefaultProvider implements DefaultValueProvider<Boolean> {
        @Override
        public Boolean get() {
            return PREFERENCE_DEFAULT_VALUE;
        }
    }
}

