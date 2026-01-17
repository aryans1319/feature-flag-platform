    package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.dto.EvaluationContextDto;
import com.aryan.featureflags.engine.RuleEvaluator;
import com.aryan.featureflags.exception.FeatureNotFoundException;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.model.Feature;
import com.aryan.featureflags.model.Rule;
import com.aryan.featureflags.repository.FeatureRepository;
import com.aryan.featureflags.repository.RuleRepository;
import com.aryan.featureflags.service.FeatureEvaluationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureEvaluationServiceImpl
        implements FeatureEvaluationService {

    private final FeatureRepository featureRepository;
    private final RuleRepository ruleRepository;
    private final RuleEvaluator ruleEvaluator;

    public FeatureEvaluationServiceImpl(
            FeatureRepository featureRepository,
            RuleRepository ruleRepository,
            RuleEvaluator ruleEvaluator) {
        this.featureRepository = featureRepository;
        this.ruleRepository = ruleRepository;
        this.ruleEvaluator = ruleEvaluator;
    }

    @Override
    public boolean evaluate(
            String featureKey,
            Environment environment,
            EvaluationContextDto context) {

        Feature feature = featureRepository
                .findByKeyAndEnvironment(featureKey, environment)
                .orElseThrow(() ->
                        new FeatureNotFoundException(
                                "Feature not found: " + featureKey
                        ));

        // 1️⃣ global toggle
        if (!feature.isEnabled()) {
            return false;
        }

        // 2️⃣ attached rules
        List<Rule> rules = ruleRepository.findByFeature(feature);
        System.out.println("RULE COUNT = " + rules.size());

        // 3️⃣ no rules → old behavior
        if (rules.isEmpty()) {
            return true;
        }

        // 4️⃣ rule match
        for (Rule rule : rules) {
            if (ruleEvaluator.matches(
                    rule,
                    context.getAttributes())) {
                return true;
            }
        }

        return false;
    }
}
