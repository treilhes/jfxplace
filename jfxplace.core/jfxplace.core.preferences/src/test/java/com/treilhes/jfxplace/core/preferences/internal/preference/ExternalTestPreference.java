package com.treilhes.jfxplace.core.preferences.internal.preference;

import com.treilhes.emc4j.boot.api.context.annotation.Singleton;
import com.treilhes.jfxplace.core.api.preference.Preference;
import com.treilhes.jfxplace.core.api.preference.PreferenceContext;
import com.treilhes.jfxplace.core.preferences.ScopedPreferenceTest.TestDefaultValueProvider;
import com.treilhes.jfxplace.core.preferences.ScopedPreferenceTest.TestValueValidator;

@Singleton
@PreferenceContext(id = "96f69947-d70f-4f95-9b1b-317aa32bfdf0", name = "some.name.global", defaultValueProvider = TestDefaultValueProvider.class, validator = TestValueValidator.class)
public interface ExternalTestPreference extends Preference<String> {
}