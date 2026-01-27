package com.aryan.featureflags.controller;

import com.aryan.featureflags.dto.ApiKeySummaryResponse;
import com.aryan.featureflags.dto.CreateApiKeyRequest;
import com.aryan.featureflags.dto.CreateApiKeyResponse;
import com.aryan.featureflags.model.ApiKey;
import com.aryan.featureflags.service.ApiKeyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/api-keys")
public class ApiKeyAdminController {
    private final ApiKeyService apiKeyService;


    public ApiKeyAdminController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }
    @PostMapping
    public ResponseEntity<CreateApiKeyResponse> create(@Valid @RequestBody CreateApiKeyRequest request){
        ApiKey apiKey= apiKeyService.create(request.getEnvironment());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreateApiKeyResponse(apiKey.getId(),
                        apiKey.getApiKey(),
                        apiKey.getEnvironment(),
                        apiKey.isEnabled()
                        )
        );
    }
    @GetMapping
    public List<ApiKeySummaryResponse> list(){
        return apiKeyService.listAll().stream().map(
                k -> new ApiKeySummaryResponse(
                        k.getId(),
                        k.getEnvironment(),
                        k.isEnabled(),
                        k.getCreatedAt()

                )).toList();
    }
    @PatchMapping("{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable Long id){
        apiKeyService.disable(id);
        return ResponseEntity.noContent().build();
    }

    // 4️⃣ Delete API key
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        apiKeyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
//de4473ce-6816-49e3-a260-ca56f1a132a0