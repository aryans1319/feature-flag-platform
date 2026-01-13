package com.aryan.featureflags.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "features")
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

    public Feature(String key, boolean enabled) {
        this.key = key;
        this.enabled = enabled;
    }

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
