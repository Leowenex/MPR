package fr.acraipea.mpr.outgateway.config;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

import java.util.List;

public record ProtocolConfiguration(
        @Nonnull String protocolName,
        @Nonnull String protocolPath,

        @Nonnull @Valid List<PartnerConfiguration> partnerConfigurations
) {}
