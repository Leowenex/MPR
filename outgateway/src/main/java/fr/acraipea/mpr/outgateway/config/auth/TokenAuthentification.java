package fr.acraipea.mpr.outgateway.config.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAuthentification {
    private String token;
    private String tokenHeaderName;
}
