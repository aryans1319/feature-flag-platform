package com.aryan.featureflags.dto;

import com.aryan.featureflags.model.Environment;
import jakarta.validation.constraints.NotNull;

public class CreateApiKeyRequest {
    @NotNull
    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }
}
