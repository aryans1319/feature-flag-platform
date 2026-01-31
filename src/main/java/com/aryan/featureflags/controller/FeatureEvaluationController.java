package com.aryan.featureflags.controller;

import com.aryan.featureflags.dto.EvaluationContextDto;
import com.aryan.featureflags.dto.EvaluationDecisionDto;
import com.aryan.featureflags.dto.FeatureResponseDto;
import com.aryan.featureflags.dto.UpdateRolloutRequestDto;
import com.aryan.featureflags.engine.RolloutEvaluator;
import com.aryan.featureflags.evaluation.EvaluationDecision;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.service.FeatureEvaluationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Map<String, Boolean>> evaluate(
            @PathVariable String key,
            @RequestParam Environment env,
            @RequestBody EvaluationContextDto context) {

        boolean result = evaluationService.evaluate(key, env, context);

        // ✅ SDK-compatible response
        return ResponseEntity.ok(
                Map.of("enabled", result)
        );
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
    @PostMapping("/{key}/evaluate/explain")
    public ResponseEntity<EvaluationDecisionDto> explainEvaluate(
            @PathVariable String key,
            @RequestParam Environment env,
            @RequestBody EvaluationContextDto context
    ) {

        EvaluationDecision decision =
                evaluationService.evaluateWithExplanation(key, env, context);

        return ResponseEntity.ok(
                new EvaluationDecisionDto(
                        decision.isEnabled(),
                        decision.getReason(),
                        decision.getMatchedRules()
                )
        );
    }

}
