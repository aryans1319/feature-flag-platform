package com.aryan.featureflags.service.impl;

import com.aryan.featureflags.model.ApiKey;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.repository.ApiKeyRepository;
import com.aryan.featureflags.service.ApiKeyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class ApiKeyServiceImpl implements ApiKeyService {
        private final ApiKeyRepository apiKeyRepository;

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public ApiKey create(Environment environment) {
        ApiKey apiKey= new ApiKey(
                UUID.randomUUID().toString(),
                environment,
                true
        );

        return apiKeyRepository.save(apiKey);
    }

    @Override
    public List<ApiKey> listAll() {


        return apiKeyRepository.findAll();
    }

    @Override
    public void disable(Long id) {
            ApiKey apiKey= apiKeyRepository.findById(id)
                    .orElseThrow( () ->
                new IllegalArgumentException("API Key Not Found: "+ id)
            );
            apiKey.setEnabled(false);
            apiKeyRepository.save(apiKey);



    }

    @Override
    public void delete(Long id) {
            apiKeyRepository.deleteById(id);
    }
}
