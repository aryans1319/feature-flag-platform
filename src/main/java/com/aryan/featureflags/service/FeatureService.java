package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.FeatureRequestDto;
import com.aryan.featureflags.dto.FeatureResponseDto;

public interface FeatureService {
    FeatureResponseDto updateFeature(String key, boolean enabled);
    FeatureResponseDto createFeature(FeatureRequestDto request);
    FeatureResponseDto getFeature(String key);
    public boolean evaluateFeature(String key);
}
