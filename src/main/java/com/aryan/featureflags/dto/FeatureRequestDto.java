package com.aryan.featureflags.dto;


import com.aryan.featureflags.model.Environment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeatureRequestDto {
    @NotBlank(message="key must not be blank")
    private String key;
    private String description;
    private boolean enabled;
    @NotNull(message = "environment must be one of [DEV, STAGING, PROD] ")
    private Environment environment;
    public FeatureRequestDto(String key, String description, boolean enabled) {
        this.key = key;
        this.description = description;
        this.enabled = enabled;
    }
    public Environment getEnvironment(){ return environment; }


    public String getKey() { return key; }
    public String getDescription() { return description; }
    public boolean isEnabled() { return enabled; }
}
