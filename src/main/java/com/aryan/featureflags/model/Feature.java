package com.aryan.featureflags.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "features",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"key", "environment"})
        }
)
public class Feature {

    @Id
    @Column(name = "key", nullable = false, updatable = false)
    private String key;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column( name= "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column( name= "updated_at", nullable = false)
    private Instant updatedAt;
    @Column(nullable = false)
    private String environment; // dev / staging / prod


    // Required by JPA
    protected Feature() {
    }

    @PrePersist
    public void OnCreate(){
        Instant now= Instant.now();
        this.createdAt= now;
        this.updatedAt= now;
    }
    @PreUpdate
    public void onUpdate(){
        this.updatedAt= Instant.now();
    }

    public Feature(String key,String environment, boolean enabled) {
        this.key = key;
        this.environment= environment;
        this.enabled = enabled;
    }

    public String getKey() {
        return key;
    }

    public String getEnvironment() {
        return environment;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
