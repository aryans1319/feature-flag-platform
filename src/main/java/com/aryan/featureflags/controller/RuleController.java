package com.aryan.featureflags.controller;

import com.aryan.featureflags.dto.RuleRequestDto;
import com.aryan.featureflags.dto.RuleResponseDto;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.service.RuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/features")
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("/{key}/rules")
    public ResponseEntity<RuleResponseDto> createRule(
            @PathVariable String key,
            @RequestParam Environment env,
            @RequestBody @Valid RuleRequestDto request) {

        RuleResponseDto response =
                ruleService.createRule(key, env, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{key}/rules")
    public ResponseEntity<List<RuleResponseDto>> listRules(
            @PathVariable String key,
            @RequestParam Environment env
    ){
        List<RuleResponseDto> rules= ruleService.listRules(key, env);
        return ResponseEntity.ok(rules);

    }


}
