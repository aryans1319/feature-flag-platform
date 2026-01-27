package com.aryan.featureflags.dto;

import com.aryan.featureflags.model.Environment;

import java.time.Instant;

public class ApiKeySummaryResponse {
    private Long id;

    public Long getId() {
        return id;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    private Environment environment;
    private boolean enabled;
    private Instant createdAt;

    public ApiKeySummaryResponse(
            Long id,
            Environment environment,
            boolean enabled,
            Instant createdAt
    ) {
        this.id = id;
        this.environment = environment;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }
}
