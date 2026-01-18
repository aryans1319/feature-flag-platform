package com.aryan.featureflags.repository;

import com.aryan.featureflags.model.Feature;
import com.aryan.featureflags.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findByFeature(Feature feature);
}

