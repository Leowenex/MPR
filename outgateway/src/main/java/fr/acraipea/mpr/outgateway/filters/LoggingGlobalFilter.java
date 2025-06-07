package fr.acraipea.mpr.outgateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
@Slf4j
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private final static String LOG = "[RouteId: {}] Request [uri: {}, method: {}, headers: {}] routed to [uri: {}, method: {}, headers: {}] with response [status: {}]";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Récupération des attributs de la requête originale passés par EntryGlobalFilter
        String originalUri = exchange.getAttribute(EntryGlobalFilter.ORIGINAL_URI_KEY);
        String originalMethod = exchange.getAttribute(EntryGlobalFilter.ORIGINAL_METHOD_KEY);
        String originalHeaders = exchange.getAttribute(EntryGlobalFilter.ORIGINAL_HEADERS_KEY);
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);

            URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
            HttpMethod transmittedMethod = exchange.getRequest().getMethod();
            HttpHeaders transmittedHeaders = exchange.getRequest().getHeaders();

            HttpStatusCode responseStatus = exchange.getResponse().getStatusCode();

            log.info(LOG,
                    Optional.ofNullable(route).map(Route::getId).orElse(null),
                    originalUri,
                    originalMethod,
                    originalHeaders,
                    routeUri,
                    transmittedMethod,
                    transmittedHeaders,
                    responseStatus
            );
        }));
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
