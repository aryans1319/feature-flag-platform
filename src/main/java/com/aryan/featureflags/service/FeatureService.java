package com.aryan.featureflags.service;

import com.aryan.featureflags.dto.FeatureResponseDto;

public interface FeatureService {
    public String updateFeature(String key, boolean enabled);
}
