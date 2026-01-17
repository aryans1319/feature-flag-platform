package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.EvaluationContextDto;
import com.aryan.featureflags.model.Environment;

public interface FeatureEvaluationService {

    boolean evaluate(
            String featureKey,
            Environment environment,
            EvaluationContextDto context
    );
}
