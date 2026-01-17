package com.aryan.featureflags.controller;

import com.aryan.featureflags.dto.EvaluationContextDto;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.service.FeatureEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/features")
public class FeatureEvaluationController {

    private final FeatureEvaluationService evaluationService;

    public FeatureEvaluationController(
            FeatureEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
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
}
