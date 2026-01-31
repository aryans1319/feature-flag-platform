package com.aryan.featureflags.dto;

import java.util.List;
import java.util.Map;

public class EvaluationDecisionDto {

    private final boolean enabled;
    private final String reason;
    private final List<Map<String, Object>> matchedRules;

    public EvaluationDecisionDto(
            boolean enabled,
            String reason,
            List<Map<String, Object>> matchedRules
    ) {
        this.enabled = enabled;
        this.reason = reason;
        this.matchedRules = matchedRules;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getReason() {
        return reason;
    }

    public List<Map<String, Object>> getMatchedRules() {
        return matchedRules;
    }
}
