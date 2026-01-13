package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.FeatureRequestDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
import com.aryan.featureflags.dto.UpdateFeatureRequestDto;
import com.aryan.featureflags.model.Environment;

public interface FeatureService {
    FeatureResponseDto updateFeature(String key, Environment environment, UpdateFeatureRequestDto request);
    FeatureResponseDto createFeature(FeatureRequestDto request);
    FeatureResponseDto getFeature(String key,Environment environment);
    public boolean evaluateFeature(String key, Environment environment);
}
