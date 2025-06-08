package fr.acraipea.mpr.outgateway.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerWebExchangeUtils {

    public static ServerWebExchange setHeader(ServerWebExchange exchange, Consumer<HttpHeaders> consumer) {
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .headers(consumer)
                .build();
        return exchange.mutate().request(request).build();
    }

    public static ServerWebExchange setHeader(ServerWebExchange exchange, String key, String value) {
        return setHeader(exchange, headers -> headers.set(key, value));
    }

    public static ServerWebExchange setBearerAuth(ServerWebExchange exchange, String token) {
        return setHeader(exchange, httpHeaders ->  httpHeaders.setBearerAuth(token));
    }

}
