package fr.acraipea.mpr.outgateway.filters.auth;

import fr.acraipea.mpr.outgateway.config.auth.PreSharedTokenAuthentification;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static fr.acraipea.mpr.outgateway.util.ServerWebExchangeUtils.setHeader;

@AllArgsConstructor
public class PreSharedTokenAuthentificationFilter implements GatewayFilter {

    private final PreSharedTokenAuthentification preSharedTokenAuthentification;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        exchange = setHeader(exchange, preSharedTokenAuthentification.tokenHeaderName(), preSharedTokenAuthentification.token());

        return chain.filter(exchange);
    }
}
