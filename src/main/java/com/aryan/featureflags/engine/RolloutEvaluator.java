package com.aryan.featureflags.engine;

import org.springframework.stereotype.Component;

@Component
public class RolloutEvaluator {

    public boolean isUserInRollout(
            String stableId,
            int rolloutPercentage) {

        if (rolloutPercentage >= 100) {
            return true;
        }

        if (rolloutPercentage <= 0) {
            return false;
        }

        // Deterministic hashing
        int hash = Math.abs(stableId.hashCode());

        int bucket = hash % 100;

        return bucket < rolloutPercentage;
    }
}
