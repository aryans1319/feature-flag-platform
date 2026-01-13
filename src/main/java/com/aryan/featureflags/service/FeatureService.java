package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.FeatureRequestDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
import com.aryan.featureflags.dto.UpdateFeatureRequestDto;

public interface FeatureService {
    FeatureResponseDto updateFeature(String key,String environment, UpdateFeatureRequestDto request);
    FeatureResponseDto createFeature(FeatureRequestDto request);
    FeatureResponseDto getFeature(String key,String environment);
    public boolean evaluateFeature(String key, String environment);
}
