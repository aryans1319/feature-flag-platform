package com.aryan.featureflags.security;

import com.aryan.featureflags.model.ApiKey;
import com.aryan.featureflags.model.Environment;
import com.aryan.featureflags.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyAuthFilter(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing API key");
            return;
        }

        String apiKeyValue = authHeader.substring(7);

        ApiKey apiKey = apiKeyRepository.findByApiKey(apiKeyValue)
                .filter(ApiKey::isEnabled)
                .orElse(null);

        if (apiKey == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid API key");
            return;
        }

        // 🔑 ONLY enforce env for evaluation endpoints
        if (request.getRequestURI().contains("/evaluate")
                || request.getRequestURI().contains("/rollout")) {

            String envParam = request.getParameter("env");
            if (envParam == null) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing env parameter");
                return;
            }

            Environment requestEnv = Environment.valueOf(envParam);

            if (apiKey.getEnvironment() != requestEnv) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(),
                        "API key not allowed for this environment");
                return;
            }
        }

        // ✅ All checks passed
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/features");
    }
}
