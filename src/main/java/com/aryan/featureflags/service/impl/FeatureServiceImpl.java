package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.dto.FeatureRequestDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
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
    public FeatureResponseDto updateFeature(String key, boolean enabled) {
        Feature feature = store.get(key);

        if (feature == null) {
            throw new FeatureNotFoundException("Feature not found: " + key);
        }

        feature.setEnabled(enabled);

        return new FeatureResponseDto(feature.getKey(), feature.isEnabled());
    }
    @Override
    public FeatureResponseDto createFeature(FeatureRequestDto request) {
        String key = request.getKey();

        if (store.containsKey(key)) {
            throw new IllegalStateException("Feature already exists: " + key);
            // later we’ll replace this with FeatureAlreadyExistsException
        }
        Feature feature = new Feature(key, request.isEnabled());
        store.put(key, feature);
        return new FeatureResponseDto(feature.getKey(), feature.isEnabled());
    }

    public FeatureResponseDto getFeature(String key){
        Feature feature = store.get(key);
        if(feature==null){
            throw new FeatureNotFoundException("Feature not found: " + key);
        }
        return new FeatureResponseDto(feature.getKey(), feature.isEnabled());
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
