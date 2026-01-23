package com.aryan.featureflags.engine;

import com.aryan.featureflags.model.Operator;
import com.aryan.featureflags.model.Rule;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class RuleEvaluator {

    public boolean matches(Rule rule, Map<String, Object> context) {

        if (context == null || context.isEmpty()) {
            return false;
        }

        Object actualValueObj = context.get(rule.getAttribute());
        if (actualValueObj == null) {
            return false;
        }

        String actualValue = actualValueObj.toString().trim();
        String ruleValue = rule.getValue().trim();

        switch (rule.getOperator()) {

            case EQUALS:
                return actualValue.equalsIgnoreCase(ruleValue);

            case IN:
                List<String> allowedValues =
                        Arrays.stream(ruleValue.split(","))
                                .map(String::trim)
                                .toList();

                return allowedValues.stream()
                        .anyMatch(v -> v.equalsIgnoreCase(actualValue));

            default:
                return false;
        }
    }
}
