package com.aryan.featureflags.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateRolloutRequestDto {

    public Integer getRollOutPercentage() {
        return rollOutPercentage;
    }

    public void setRollOutPercentage(Integer rollOutPercentage) {
        this.rollOutPercentage = rollOutPercentage;
    }

    @NotNull
    @Min(0)
    @Max(100)

    private Integer rollOutPercentage;

}
