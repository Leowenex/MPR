package fr.acraipea.mpr.outgateway.config;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProtocolConfiguration {
    @Nonnull @Valid
    private String protocolName;
    @Nonnull @Valid
    private String protocolPath;
    @Nonnull @Valid
    private List<PartnerConfiguration> partnerConfigurations;
}
