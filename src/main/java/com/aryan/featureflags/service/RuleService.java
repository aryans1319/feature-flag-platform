package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.RuleRequestDto;
import com.aryan.featureflags.dto.RuleResponseDto;
import com.aryan.featureflags.model.Environment;

public interface RuleService {

    RuleResponseDto createRule(
            String featureKey,
            Environment environment,
            RuleRequestDto request);
}
