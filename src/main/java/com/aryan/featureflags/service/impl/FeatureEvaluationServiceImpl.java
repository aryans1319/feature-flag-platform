package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.dto.EvaluationContextDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
import com.aryan.featureflags.dto.UpdateRolloutRequestDto;
import com.aryan.featureflags.engine.RuleEvaluator;
import com.aryan.featureflags.engine.RolloutEvaluator;
import com.aryan.featureflags.evaluation.EvaluationDecision;
import com.aryan.featureflags.exception.FeatureNotFoundException;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.model.Feature;
import com.aryan.featureflags.model.Rule;
import com.aryan.featureflags.repository.FeatureRepository;
import com.aryan.featureflags.repository.RuleRepository;
import com.aryan.featureflags.service.FeatureEvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
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

    private final RedisTemplate<String,Object> redisTemplate;

    public FeatureEvaluationServiceImpl(
            FeatureRepository featureRepository,
            RuleRepository ruleRepository,
            RuleEvaluator ruleEvaluator,
            RolloutEvaluator rolloutEvaluator, RedisTemplate<String, Object> redisTemplate) {

        this.featureRepository = featureRepository;
        this.ruleRepository = ruleRepository;
        this.ruleEvaluator = ruleEvaluator;
        this.rolloutEvaluator = rolloutEvaluator;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean evaluate(
            String featureKey,
            Environment environment,
            EvaluationContextDto context) {

        log.info(">>> LOOKUP KEY='{}', ENV='{}'", featureKey, environment);

        String userID= null;
        if(context!=null && context.getAttributes()!=null){
            Object id= context.getAttributes().get("userId");
            if(id!=null){
                userID= id.toString();
            }
        }
//        Build Cache Key
        String cacheKey= "feature:" + featureKey + ":" +environment+
                (userID!= null ? ":" +userID: "");

        log.info(">>> CACHE KEY = {}", cacheKey);

        String cachedValue = (String) redisTemplate.opsForValue().get(cacheKey);
        if (cachedValue != null) {
            log.info(">>> CACHE HIT → returning {}", cachedValue);
            return Boolean.parseBoolean(cachedValue);
        }


        Feature feature = featureRepository
                .findByKeyAndEnvironment(featureKey, environment)
                .orElseThrow(() ->
                        new FeatureNotFoundException(
                                "Feature not found: " + featureKey
                        ));

        log.info(
                ">>> FEATURE FOUND: id={}, key={}, env={}, enabled={}, rollout={}",
                feature.getId(),
                feature.getKey(),
                feature.getEnvironment(),
                feature.isEnabled(),
                feature.getRolloutPercentage()
        );

        /* 1️⃣ Global toggle */
        if (!feature.isEnabled()) {
            log.info(">>> FEATURE DISABLED");
            redisTemplate.opsForValue().set(cacheKey, String.valueOf(false));   //storing result in redis
            return false;
        }

        Map<String, Object> attributes =
                context != null ? context.getAttributes() : null;

        /* 2️⃣ Percentage rollout gate */
        Integer rollout = feature.getRolloutPercentage();

        if (rollout != null && rollout < 100) {

            if (attributes == null || !attributes.containsKey("userId")) {
                log.info(">>> ROLLOUT BLOCKED: missing userId");
                return false;
            }

            String userId = attributes.get("userId").toString();

            boolean inRollout =
                    rolloutEvaluator.isUserInRollout(userId, rollout);

            log.info(">>> ROLLOUT CHECK: userId={}, inRollout={}",
                    userId, inRollout);

            if (!inRollout) {
                return false;
            }
        }

        /* 3️⃣ Rule evaluation */
        List<Rule> rules = ruleRepository.findByFeature(feature);

        log.info(">>> RULE COUNT = {}", rules.size());

        if (rules.isEmpty()) {
            log.info(">>> NO RULES → FEATURE ON");
            redisTemplate.opsForValue()
                    .set(cacheKey, String.valueOf(true), java.time.Duration.ofMinutes(5));
            return true;
        }

        for (Rule rule : rules) {
            boolean matched = ruleEvaluator.matches(rule, attributes);
            log.info(
                    ">>> RULE CHECK: attr={}, op={}, value={}, matched={}",
                    rule.getAttribute(),
                    rule.getOperator(),
                    rule.getValue(),
                    matched
            );

            if (matched) {
                redisTemplate.opsForValue()
                        .set(cacheKey, String.valueOf(true),
                                java.time.Duration.ofMinutes(5));
                return true;
            }
        }

        log.info(">>> NO RULE MATCHED → FEATURE OFF");
        redisTemplate.opsForValue().set(cacheKey, String.valueOf(false));
        return false;
    }

    @Override
    public FeatureResponseDto updateRolloutPercentage(String key, Environment environment, UpdateRolloutRequestDto request) {
        Feature feature= featureRepository.findByKeyAndEnvironment(key, environment).orElseThrow(()->
                new FeatureNotFoundException(
                        "Feature not found" + key+ "for env: " + environment
                ));

        feature.setRolloutPercentage(request.getRollOutPercentage());
        Feature saved= featureRepository.save(feature);

        String pattern = "feature:" + key + ":" + environment + "*";
        redisTemplate.keys(pattern)
                .forEach(redisTemplate::delete);

        return null;
    }

    @Override
    public EvaluationDecision evaluateWithExplanation(
            String featureKey,
            Environment environment,
            EvaluationContextDto context
    ) {

        Feature feature = featureRepository
                .findByKeyAndEnvironment(featureKey, environment)
                .orElseThrow(() ->
                        new FeatureNotFoundException(
                                "Feature not found: " + featureKey
                        )
                );

        if (!feature.isEnabled()) {
            return new EvaluationDecision(
                    false,
                    "FEATURE_DISABLED",
                    List.of()
            );
        }

        Map<String, Object> attributes =
                context != null ? context.getAttributes() : null;

        Integer rollout = feature.getRolloutPercentage();

        if (rollout != null && rollout < 100) {

            if (attributes == null || !attributes.containsKey("userId")) {
                return new EvaluationDecision(
                        false,
                        "ROLLOUT_EXCLUDED",
                        List.of()
                );
            }

            String userId = attributes.get("userId").toString();

            boolean inRollout =
                    rolloutEvaluator.isUserInRollout(userId, rollout);

            if (!inRollout) {
                return new EvaluationDecision(
                        false,
                        "ROLLOUT_EXCLUDED",
                        List.of(
                                Map.of("rolloutPercentage", rollout)
                        )
                );
            }
        }

        List<Rule> rules = ruleRepository.findByFeature(feature);

        if (rules.isEmpty()) {
            return new EvaluationDecision(
                    true,
                    "NO_RULES_DEFINED",
                    List.of()
            );
        }

        List<Map<String, Object>> matchedRules = new java.util.ArrayList<>();

        for (Rule rule : rules) {
            boolean matched = ruleEvaluator.matches(rule, attributes);

            if (matched) {
                matchedRules.add(
                        Map.of(
                                "attribute", rule.getAttribute(),
                                "operator", rule.getOperator().name(),
                                "value", rule.getValue()
                        )
                );
            }
        }

        if (!matchedRules.isEmpty()) {
            return new EvaluationDecision(
                    true,
                    "RULE_MATCHED",
                    matchedRules
            );
        }

        return new EvaluationDecision(
                false,
                "NO_RULE_MATCHED",
                List.of()
        );
    }


}
