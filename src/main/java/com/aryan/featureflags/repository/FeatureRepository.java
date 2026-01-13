package com.aryan.featureflags.repository;

import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature, String> {
    Optional<Feature> findByKeyAndEnvironment(String key, Environment environment);
}
