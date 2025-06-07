package fr.acraipea.mpr.outgateway.config;

import fr.acraipea.mpr.outgateway.config.auth.TokenAuthentification;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerConfiguration {
    @Nonnull
    private String partnerCode;
    @Nonnull
    private String partnerUri;

    @Nullable
    private TokenAuthentification tokenAuthentication;
}
