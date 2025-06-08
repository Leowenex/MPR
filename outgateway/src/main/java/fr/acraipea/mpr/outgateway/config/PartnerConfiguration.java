package fr.acraipea.mpr.outgateway.config;

import fr.acraipea.mpr.outgateway.config.auth.BearerTokenAuthentification;
import fr.acraipea.mpr.outgateway.config.auth.PreSharedTokenAuthentification;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;

public record PartnerConfiguration (
    @Nonnull String partnerCode,
    @Nonnull String partnerUri,

    @Nullable @Valid PreSharedTokenAuthentification preSharedTokenAuthentification,
    @Nullable @Valid BearerTokenAuthentification bearerTokenAuthentication
) {}
