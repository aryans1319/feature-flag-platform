package com.aryan.featureflags.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "api_keys")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key", nullable = false, unique = true)
    private String apiKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "environment", nullable = false)
    private Environment environment;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ApiKey() {}

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
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
}
