package com.aryan.featureflags.dto;

import com.aryan.featureflags.model.Environment;

public class FeatureResponseDto {
    private final String key;
    private final boolean enabled;
    private Integer rollOutPercentage;
    private Environment environment;

    public FeatureResponseDto(String key, Environment environment, boolean enabled, Integer rollOutPercentage) {
        this.key = key;
        this.environment= environment;
        this.enabled = enabled;
        this.rollOutPercentage= rollOutPercentage;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }
    public Integer getRollOutPercentage() {
        return rollOutPercentage;
    }
}
