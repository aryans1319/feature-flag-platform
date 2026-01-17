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

        // Fetch the value from request for this rule’s attribute
        Object actualValue = context.get(rule.getAttribute());
        if (actualValue == null) {
            return false;
        }

        // Compare based on operator
        switch (rule.getOperator()) {

            case EQUALS:
                return actualValue.toString()
                        .equalsIgnoreCase(rule.getValue());

            case IN:
                List<String> allowedValues =
                        Arrays.stream(rule.getValue().split(","))
                                .map(String::trim)
                                .toList();

                return allowedValues.contains(
                        actualValue.toString()
                );

            default:
                return false;
        }
    }
}
