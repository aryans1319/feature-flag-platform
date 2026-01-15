package com.aryan.featureflags.dto;

import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.model.Operator;

import java.time.Instant;

public class RuleResponseDto {

    private Long id;
    private String featureKey;
    private Environment environment;
    private String attribute;
    private Operator operator;
    private String value;
    private Instant createdAt;

    public RuleResponseDto(
            Long id,
            String featureKey,
            Environment environment,
            String attribute,
            Operator operator,
            String value,
            Instant createdAt) {
        this.id = id;
        this.featureKey = featureKey;
        this.environment = environment;
        this.attribute = attribute;
        this.operator = operator;
        this.value = value;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getFeatureKey() {
        return featureKey;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getAttribute() {
        return attribute;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
