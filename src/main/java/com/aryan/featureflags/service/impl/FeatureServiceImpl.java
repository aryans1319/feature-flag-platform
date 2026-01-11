package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.dto.FeatureRequestDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
import com.aryan.featureflags.exception.FeatureAlreadyExistsException;
import com.aryan.featureflags.exception.FeatureNotFoundException;
import com.aryan.featureflags.model.Feature;
import com.aryan.featureflags.repository.FeatureRepository;
import com.aryan.featureflags.service.FeatureService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FeatureServiceImpl implements FeatureService {

    /**
     * SHARED IN-MEMORY STORE
     * This will be reused when CREATE is added
     *
     *
     */
    private final Map<String, Feature> store = new ConcurrentHashMap<>();

    private final FeatureRepository featureRepository;

    /**
     * TEMP SEED DATA
     * Allows UPDATE to be tested before CREATE exists
     * Remove after create API is created
     */
    public FeatureServiceImpl(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
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

        featureRepository.findByKey(key)
                .ifPresent(feature -> {
                    throw new FeatureAlreadyExistsException(
                            "Feature already exists: " + key
                    );
                });

        Feature feature = new Feature(
                request.getKey(),

                request.isEnabled()
        );

        Feature savedFeature = featureRepository.save(feature);

        return new FeatureResponseDto(
                savedFeature.getKey(),
                savedFeature.isEnabled()
        );
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
