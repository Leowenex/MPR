package fr.acraipea.mpr.outgateway.config.auth;

import jakarta.annotation.Nonnull;

import java.util.Map;

public record BearerTokenAuthentification(
        @Nonnull Map<String, String> requestBody,
        @Nonnull String requestUrl,
        @Nonnull String tokenRequestResponseField
) {}
