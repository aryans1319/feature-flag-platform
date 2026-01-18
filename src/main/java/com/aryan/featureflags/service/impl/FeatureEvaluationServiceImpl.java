package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.dto.EvaluationContextDto;
import com.aryan.featureflags.engine.RuleEvaluator;
import com.aryan.featureflags.engine.RolloutEvaluator;
import com.aryan.featureflags.exception.FeatureNotFoundException;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.model.Feature;
import com.aryan.featureflags.model.Rule;
import com.aryan.featureflags.repository.FeatureRepository;
import com.aryan.featureflags.repository.RuleRepository;
import com.aryan.featureflags.service.FeatureEvaluationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeatureEvaluationServiceImpl
        implements FeatureEvaluationService {

    private final FeatureRepository featureRepository;
    private final RuleRepository ruleRepository;
    private final RuleEvaluator ruleEvaluator;
    private final RolloutEvaluator rolloutEvaluator;

    public FeatureEvaluationServiceImpl(
            FeatureRepository featureRepository,
            RuleRepository ruleRepository,
            RuleEvaluator ruleEvaluator,
            RolloutEvaluator rolloutEvaluator) {

        this.featureRepository = featureRepository;
        this.ruleRepository = ruleRepository;
        this.ruleEvaluator = ruleEvaluator;
        this.rolloutEvaluator = rolloutEvaluator;
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

        /* 1️⃣ Global toggle */
        if (!feature.isEnabled()) {
            return false;
        }

        Map<String, Object> attributes =
                context != null ? context.getAttributes() : null;

        /* 2️⃣ Percentage rollout gate */
        Integer rollout = feature.getRolloutPercentage();

        if (rollout != null && rollout < 100) {

            if (attributes == null
                    || !attributes.containsKey("userId")) {
                // Cannot evaluate rollout without stable identifier
                return false;
            }

            String userId = attributes.get("userId").toString();

            boolean inRollout =
                    rolloutEvaluator.isUserInRollout(userId, rollout);

            if (!inRollout) {
                return false;
            }
        }

        /* 3️⃣ Rule evaluation */
        List<Rule> rules = ruleRepository.findByFeature(feature);

        if (rules.isEmpty()) {
            return true;
        }

        for (Rule rule : rules) {
            if (ruleEvaluator.matches(rule, attributes)) {
                return true;
            }
        }

        return false;
    }
}
