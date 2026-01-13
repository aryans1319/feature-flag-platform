package com.aryan.featureflags.controller;

import com.aryan.featureflags.dto.FeatureRequestDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
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

    // UPDATE
    @PutMapping("/{key}")
    public ResponseEntity<FeatureResponseDto> updateFeature(
            @PathVariable String key,
            @RequestParam String env,
            @RequestBody UpdateFeatureRequestDto request) {

        FeatureResponseDto response =
                featureService.updateFeature(key, env,request);

        return ResponseEntity.ok(response);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<FeatureResponseDto> createFeature(
            @RequestBody FeatureRequestDto request) {

        FeatureResponseDto response = featureService.createFeature(request);
        return ResponseEntity.status(201).body(response);
    }

    // GET (configuration)
    @GetMapping("/{key}")
    public ResponseEntity<FeatureResponseDto> getFeature(@PathVariable String key, @RequestParam String env) {
        FeatureResponseDto response = featureService.getFeature(key,env);
        return ResponseEntity.ok(response);
    }

    // EVALUATE (decision)
    @GetMapping("/{key}/evaluate")
    public ResponseEntity<Boolean> evaluateFeature(@PathVariable String key, @RequestParam String env) {
        boolean enabled = featureService.evaluateFeature(key, env);
        return ResponseEntity.ok(enabled);
    }
}
