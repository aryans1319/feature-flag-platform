package com.aryan.featureflags.dto;

import com.aryan.featureflags.model.Operator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RuleRequestDto {

    @NotBlank
    private String attribute;

    @NotNull
    private Operator operator;

    @NotBlank
    private String value;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
