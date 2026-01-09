package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.FeatureRequestDto;
import com.aryan.featureflags.dto.FeatureResponseDto;

public interface FeatureService {
    public String updateFeature(String key, boolean enabled);
    public String createFeature(FeatureRequestDto request);
    FeatureResponseDto evaluateFeature(String key);


}
