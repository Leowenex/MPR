package fr.acraipea.mpr.outgateway.filters.logging;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class EntryGlobalFilter implements GlobalFilter, Ordered {

    public static final String ORIGINAL_URI_KEY = "URI";
    public static final String ORIGINAL_METHOD_KEY = "METHOD";
    public static final String ORIGINAL_HEADERS_KEY = "HEADERS";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Set context for LoggingGlobalFilter
        exchange.getAttributes().put(ORIGINAL_URI_KEY, exchange.getRequest().getURI().toString());
        exchange.getAttributes().put(ORIGINAL_METHOD_KEY, String.valueOf(exchange.getRequest().getMethod()));
        exchange.getAttributes().put(ORIGINAL_HEADERS_KEY, exchange.getRequest().getHeaders().toString());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
