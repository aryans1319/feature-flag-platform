package com.aryan.featureflags.controller;

import com.aryan.featureflags.dto.EvaluationContextDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
import com.aryan.featureflags.dto.UpdateRolloutRequestDto;
import com.aryan.featureflags.engine.RolloutEvaluator;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.service.FeatureEvaluationService;
import jakarta.validation.Valid;
import org.hibernate.generator.values.internal.GeneratedValuesImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/features")
public class FeatureEvaluationController {

    private final FeatureEvaluationService evaluationService;
    private final RolloutEvaluator rolloutEvaluator;

    public FeatureEvaluationController(
            FeatureEvaluationService evaluationService, RolloutEvaluator rolloutEvaluator) {
        this.evaluationService = evaluationService;
        this.rolloutEvaluator = rolloutEvaluator;
    }

    @PostMapping("/{key}/evaluate")
    public ResponseEntity<Boolean> evaluate(
            @PathVariable String key,
            @RequestParam Environment env,
            @RequestBody EvaluationContextDto context) {

        boolean result =
                evaluationService.evaluate(key, env, context);

        return ResponseEntity.ok(result);
    }
    @PutMapping("/{key}/rollout")
    public ResponseEntity<FeatureResponseDto> updateRollout(
            @PathVariable String key,
            @RequestParam Environment env,
            @Valid @RequestBody UpdateRolloutRequestDto request
    ){
        FeatureResponseDto response= evaluationService.updateRolloutPercentage(key, env, request);

        return ResponseEntity.ok(response);



    }
}
