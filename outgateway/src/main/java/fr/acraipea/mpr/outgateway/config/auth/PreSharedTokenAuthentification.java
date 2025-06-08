package fr.acraipea.mpr.outgateway.config.auth;

import jakarta.annotation.Nonnull;

public record PreSharedTokenAuthentification(
        @Nonnull String token,
        @Nonnull String tokenHeaderName
) {}
