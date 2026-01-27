package com.aryan.featureflags.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "api_keys")
public class ApiKey {

    public Long getId() {
        return id;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
//    required by JPA Hibernate creates entity objects using reflection, not constructors.
    protected ApiKey() {}

    public ApiKey(String apiKey, Environment environment, boolean enabled) {
        this.apiKey = apiKey;
        this.environment = environment;
        this.enabled = enabled;
    }


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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
