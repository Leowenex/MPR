package fr.acraipea.mpr.partnermock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "crkphoto")
public class CriPhotoConfiguration {

    @Getter
    @Setter
    private String outGatewayUrl;

    @Getter
    @Setter
    private String localPartnerCode;

}
