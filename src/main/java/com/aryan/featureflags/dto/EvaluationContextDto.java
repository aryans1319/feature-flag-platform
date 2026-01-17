package com.aryan.featureflags.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class EvaluationContextDto {

    @NotNull
    private Map<String, Object> attributes;

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
