package com.aryan.featureflags.model;

public class Feature {

    private String key;
    private boolean enabled;

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
