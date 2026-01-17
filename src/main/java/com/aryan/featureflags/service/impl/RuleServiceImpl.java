package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.dto.RuleRequestDto;
import com.aryan.featureflags.dto.RuleResponseDto;
import com.aryan.featureflags.exception.FeatureNotFoundException;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.model.Feature;
import com.aryan.featureflags.model.Rule;
import com.aryan.featureflags.repository.FeatureRepository;
import com.aryan.featureflags.repository.RuleRepository;
import com.aryan.featureflags.service.RuleService;
import org.hibernate.generator.values.internal.GeneratedValuesImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {

    private final FeatureRepository featureRepository;
    private final RuleRepository ruleRepository;

    public RuleServiceImpl(
            FeatureRepository featureRepository,
            RuleRepository ruleRepository) {
        this.featureRepository = featureRepository;
        this.ruleRepository = ruleRepository;
    }

    @Override
    public RuleResponseDto createRule(
            String featureKey,
            Environment environment,
            RuleRequestDto request) {

        Feature feature = featureRepository
                .findByKeyAndEnvironment(featureKey, environment)
                .orElseThrow(() ->
                        new FeatureNotFoundException(
                                "Feature not found: " + featureKey +
                                        " for env: " + environment
                        ));

        Rule rule = new Rule();
        rule.setFeature(feature);
        rule.setAttribute(request.getAttribute());
        rule.setOperator(request.getOperator());
        rule.setValue(request.getValue());

        Rule savedRule = ruleRepository.save(rule);

        return new RuleResponseDto(
                savedRule.getId(),
                feature.getKey(),
                feature.getEnvironment(),
                savedRule.getAttribute(),
                savedRule.getOperator(),
                savedRule.getValue(),
                savedRule.getCreatedAt()
        );
    }
    public List<RuleResponseDto> listRules(String featureKey, Environment environment){
        Feature feature= featureRepository.findByKeyAndEnvironment(featureKey, environment)
                .orElseThrow(()->
                        new FeatureNotFoundException(
                                "Feature not found" + featureKey+ "for env: " + environment
                        )

                        );
           List<Rule> rules= ruleRepository.findByFeature(feature);
           return rules.stream()
                   .map(rule -> new RuleResponseDto(
                           rule.getId(),
                           feature.getKey(),
                           feature.getEnvironment(),
                           rule.getAttribute(),
                           rule.getOperator(),
                           rule.getValue(),
                           rule.getCreatedAt()


                   )).toList();

    }

    public void deleteRule(String featureKey, Environment env, Long ruleId){
//Finding feature
        Feature feature= featureRepository.findByKeyAndEnvironment(featureKey, env)
                .orElseThrow(()->
                        new FeatureNotFoundException(
                                "Feature not found: " + featureKey +
                                        " for env: " + env
                        ));
//        Finding Rule
        Rule rule= ruleRepository.findById(ruleId)
                .orElseThrow(()->
                        new FeatureNotFoundException(
                                "Rule not found: " + ruleId

                        ));
        if(!rule.getFeature().getId().equals(feature.getId())){
            throw new FeatureNotFoundException(
                    "Rule doesnt belong to feature: " + featureKey
            );
        }
        ruleRepository.delete(rule);
    }
}
