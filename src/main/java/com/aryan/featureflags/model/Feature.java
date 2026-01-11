package com.aryan.featureflags.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "features")
public class Feature {

    @Id
    @Column(name = "key", nullable = false, updatable = false)
    private String key;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    // Required by JPA
    protected Feature() {
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
