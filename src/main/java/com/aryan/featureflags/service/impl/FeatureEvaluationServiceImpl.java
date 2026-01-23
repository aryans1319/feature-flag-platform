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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeatureEvaluationServiceImpl implements FeatureEvaluationService {

    private static final Logger log =
            LoggerFactory.getLogger(FeatureEvaluationServiceImpl.class);

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

        // 🔍 LOG 1: What is being evaluated
        log.error(">>> LOOKUP KEY='{}', ENV='{}'", featureKey, environment);

        Feature feature = featureRepository
                .findByKeyAndEnvironment(featureKey, environment)
                .orElseThrow(() ->
                        new FeatureNotFoundException(
                                "Feature not found: " + featureKey
                        ));

        // 🔍 LOG 2: Feature found
        log.error(
                ">>> FEATURE FOUND: id={}, key={}, env={}, enabled={}, rollout={}",
                feature.getId(),
                feature.getKey(),
                feature.getEnvironment(),
                feature.isEnabled(),
                feature.getRolloutPercentage()
        );

        /* 1️⃣ Global toggle */
        if (!feature.isEnabled()) {
            log.error(">>> FEATURE DISABLED");
            return false;
        }

        Map<String, Object> attributes =
                context != null ? context.getAttributes() : null;

        /* 2️⃣ Percentage rollout gate */
        Integer rollout = feature.getRolloutPercentage();

        if (rollout != null && rollout < 100) {

            if (attributes == null || !attributes.containsKey("userId")) {
                log.error(">>> ROLLOUT BLOCKED: missing userId");
                return false;
            }

            String userId = attributes.get("userId").toString();

            boolean inRollout =
                    rolloutEvaluator.isUserInRollout(userId, rollout);

            log.error(">>> ROLLOUT CHECK: userId={}, inRollout={}",
                    userId, inRollout);

            if (!inRollout) {
                return false;
            }
        }

        /* 3️⃣ Rule evaluation */
        List<Rule> rules = ruleRepository.findByFeature(feature);

        log.error(">>> RULE COUNT = {}", rules.size());

        if (rules.isEmpty()) {
            log.error(">>> NO RULES → FEATURE ON");
            return true;
        }

        for (Rule rule : rules) {
            boolean matched = ruleEvaluator.matches(rule, attributes);
            log.error(
                    ">>> RULE CHECK: attr={}, op={}, value={}, matched={}",
                    rule.getAttribute(),
                    rule.getOperator(),
                    rule.getValue(),
                    matched
            );

            if (matched) {
                return true;
            }
        }

        log.error(">>> NO RULE MATCHED → FEATURE OFF");
        return false;
    }
}
