package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.EvaluationContextDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
import com.aryan.featureflags.dto.UpdateRolloutRequestDto;
import com.aryan.featureflags.model.Environment;

public interface FeatureEvaluationService {

    boolean evaluate(
            String featureKey,
            Environment environment,
            EvaluationContextDto context
    );

    FeatureResponseDto updateRolloutPercentage(String key, Environment environment, UpdateRolloutRequestDto request);
}
