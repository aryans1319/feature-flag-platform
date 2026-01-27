package com.aryan.featureflags.dto;

import com.aryan.featureflags.model.Environment;

public class CreateApiKeyResponse {
    private Long id;
    private String apiKey;   // raw key

    public Long getId() {
        return id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public boolean isEnabled() {
        return enabled;
    }

    private Environment environment;
    private boolean enabled;

    public CreateApiKeyResponse(
            Long id,
            String apiKey,
            Environment environment,
            boolean enabled
    ) {
        this.id = id;
        this.apiKey = apiKey;
        this.environment = environment;
        this.enabled = enabled;
    }
}
