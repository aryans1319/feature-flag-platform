package com.aryan.featureflags.dto;

public class FeatureResponseDto {
    private final String key;
    private final boolean enabled;

    public FeatureResponseDto(String key, boolean enabled) {
        this.key = key;
        this.enabled = enabled;
    }

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
