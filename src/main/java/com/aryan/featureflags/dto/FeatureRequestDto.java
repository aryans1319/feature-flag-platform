package com.aryan.featureflags.dto;

public class FeatureRequestDto {
    private String key;
    private String description;
    private boolean enabled;
    public FeatureRequestDto(String key, String description, boolean enabled) {
        this.key = key;
        this.description = description;
        this.enabled = enabled;
    }

    public String getKey() { return key; }
    public String getDescription() { return description; }
    public boolean isEnabled() { return enabled; }
}
