package com.aryan.featureflags.dto;

public class FeatureResponseDto {
    private final String key;
    private final boolean enabled;

    private String environment;

    public FeatureResponseDto(String key, String environment, boolean enabled) {
        this.key = key;
        this.environment= environment;
        this.enabled = enabled;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
