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
    public FeatureResponseDto updateFeature(String key,String environment, UpdateFeatureRequestDto request) {
        Feature feature = featureRepository.findByKeyAndEnvironment(key, environment)
                .orElseThrow(() ->
                        new FeatureNotFoundException("Feature not found: " + key + "for env: "+ environment)
                );
        feature.setEnabled(request.isEnabled());
        Feature updated = featureRepository.save(feature);
        return new FeatureResponseDto(updated.getKey(),  updated.getEnvironment(),updated.isEnabled());
    }

    @Override
    public FeatureResponseDto createFeature(FeatureRequestDto request) {
        String key = request.getKey();

        featureRepository.findByKeyAndEnvironment(key, request.getEnvironment())
                .ifPresent(feature -> {
                    throw new FeatureAlreadyExistsException(
                            "Feature already exists for env: " + request.getEnvironment()
                    );
                });

        Feature feature = new Feature(
                request.getKey(),
                request.getEnvironment(),
                request.isEnabled()
        );

        Feature savedFeature = featureRepository.save(feature);

        return new FeatureResponseDto(
                savedFeature.getKey(),
                savedFeature.getEnvironment(),
                savedFeature.isEnabled()
        );
    }

    @Override
    public FeatureResponseDto getFeature(String key, String environment) {

        Feature feature = featureRepository.findByKeyAndEnvironment(key, environment)
                .orElseThrow(() ->
                        new FeatureNotFoundException("Feature not found: " + key + "for env: "+ environment)
                );

        return new FeatureResponseDto(feature.getKey(), feature.getEnvironment(), feature.isEnabled());
    }

    @Override
    public boolean evaluateFeature(String key, String environment) {

        Feature feature = featureRepository.findByKeyAndEnvironment(key,environment)
                .orElseThrow(() ->
                        new FeatureNotFoundException("Feature not found: " + key)
                );

        return feature.isEnabled();
    }
}
