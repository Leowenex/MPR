package fr.acraipea.mpr.outgateway.config;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "gateway")
@Getter
@Setter
public class GatewayConfiguration {

    @Nonnull @Valid private List<ProtocolConfiguration> protocols;

}
