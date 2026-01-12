package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.dto.FeatureRequestDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
import com.aryan.featureflags.dto.UpdateFeatureRequestDto;
import com.aryan.featureflags.exception.FeatureAlreadyExistsException;
import com.aryan.featureflags.exception.FeatureNotFoundException;
import com.aryan.featureflags.model.Feature;
import com.aryan.featureflags.repository.FeatureRepository;
import com.aryan.featureflags.service.FeatureService;
import org.springframework.stereotype.Service;


@Service
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    public FeatureServiceImpl(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public FeatureResponseDto updateFeature(String key, UpdateFeatureRequestDto request) {
        Feature feature = featureRepository.findById(key)
                .orElseThrow(() ->
                        new FeatureNotFoundException("Feature not found: " + key)
                );
        feature.setEnabled(request.isEnabled());
        Feature updated = featureRepository.save(feature);
        return new FeatureResponseDto(updated.getKey(), updated.isEnabled());
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

    @Override
    public FeatureResponseDto getFeature(String key) {

        Feature feature = featureRepository.findById(key)
                .orElseThrow(() ->
                        new FeatureNotFoundException("Feature not found: " + key)
                );

        return new FeatureResponseDto(feature.getKey(), feature.isEnabled());
    }

    @Override
    public boolean evaluateFeature(String key) {

        Feature feature = featureRepository.findById(key)
                .orElseThrow(() ->
                        new FeatureNotFoundException("Feature not found: " + key)
                );

        return feature.isEnabled();
    }
}
