package com.aryan.featureflags.controller;

import com.aryan.featureflags.dto.UpdateFeatureRequestDto;
import com.aryan.featureflags.service.FeatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/features")
public class FeatureController {

    private final FeatureService featureService;

    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @PutMapping("/{key}")
    public ResponseEntity<String> updateFeature(
            @PathVariable String key,
            @RequestBody UpdateFeatureRequestDto request) {

        String result = featureService.updateFeature(key, request.isEnabled());

        if (result.startsWith("Feature not found")) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.ok(result);
    }
}
