package com.aryan.featureflags.service;

import com.aryan.featureflags.model.ApiKey;
import com.aryan.featureflags.model.Environment;

import java.util.List;

public interface ApiKeyService {

    ApiKey create(Environment environment);

    List<ApiKey> listAll();
    void disable(Long id);

    void delete(Long id);



}
