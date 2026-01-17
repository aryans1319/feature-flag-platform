package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.RuleRequestDto;
import com.aryan.featureflags.dto.RuleResponseDto;
import com.aryan.featureflags.model.Environment;

import java.util.List;

public interface RuleService {

    RuleResponseDto createRule(
            String featureKey,
            Environment environment,
            RuleRequestDto request);
    List<RuleResponseDto> listRules(String featureKey, Environment env);
    void deleteRule(String featureKey, Environment environment, Long ruleId);



}
