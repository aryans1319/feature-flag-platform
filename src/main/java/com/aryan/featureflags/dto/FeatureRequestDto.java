package com.aryan.featureflags.dto;

import com.aryan.featureflags.model.Environment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeatureRequestDto {

    @NotBlank(message = "key must not be blank")
    private String key;

    private String description;

    private boolean enabled;

    @NotNull(message = "environment must be one of [DEV, STAGING, PROD]")
    private Environment environment;

    // ✅ REQUIRED for Jackson
    public FeatureRequestDto() {
    }

    // Optional convenience constructor (not used by API)
    public FeatureRequestDto(String key, String description, boolean enabled, Environment environment) {
        this.key = key;
        this.description = description;
        this.enabled = enabled;
        this.environment = environment;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
