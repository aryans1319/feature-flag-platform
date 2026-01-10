package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.exception.FeatureNotFoundException;
import com.aryan.featureflags.model.Feature;
import com.aryan.featureflags.service.FeatureService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FeatureServiceImpl implements FeatureService {

    /**
     * SHARED IN-MEMORY STORE
     * This will be reused when CREATE is added
     */
    private final Map<String, Feature> store = new ConcurrentHashMap<>();

    /**
     * TEMP SEED DATA
     * Allows UPDATE to be tested before CREATE exists
     * Remove after create API is created
     */
    public FeatureServiceImpl() {
        store.put("NEW_CHECKOUT", new Feature("NEW_CHECKOUT", false));
        store.put("TEST_UPDATE", new Feature("TEST_UPDATE", false));
    }

    @Override
    public String updateFeature(String key, boolean enabled) {
        Feature feature = store.get(key);
        if (feature == null) {
            return "Feature not found: " + key;
        }
        feature.setEnabled(enabled);
        return "Feature updated successfully";
    }

    @Override
    public boolean evaluateFeature(String key){
        Feature feature = store.get(key);
        if (feature == null) {
            throw new FeatureNotFoundException("Feature not found: " + key);
        }
        return feature.isEnabled();
    }
}
