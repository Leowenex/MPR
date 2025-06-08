package fr.acraipea.mpr.outgateway.filters.auth;

import fr.acraipea.mpr.outgateway.config.auth.BearerTokenAuthentification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

import static fr.acraipea.mpr.outgateway.util.ServerWebExchangeUtils.setBearerAuth;

@Slf4j
public class BearerTokenAuthentificationFilter implements GatewayFilter {

    private final WebClient webClient;
    private final BearerTokenAuthentification bearerTokenAuthentication;

    public BearerTokenAuthentificationFilter(BearerTokenAuthentification bearerTokenAuthentication) {
        this.bearerTokenAuthentication = bearerTokenAuthentication;
        this.webClient = WebClient.create(bearerTokenAuthentication.requestUrl());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<>() {};
        return webClient
                .post()
                .body(Mono.just(bearerTokenAuthentication.requestBody()), Map.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        exceptionFromErrorResponse(clientResponse, bearerTokenAuthentication.requestUrl())
                )
                .bodyToMono(responseType)
                .map(response -> {
                    String token = response.get(bearerTokenAuthentication.tokenRequestResponseField());
                    if (token != null) {
                        return setBearerAuth(exchange, token);
                    }
                    return exchange;
                })
                .onErrorResume(e -> {
                    log.warn("Failed to retrieve bearer token from authentication service {}", bearerTokenAuthentication.requestUrl(), e);
                    return Mono.just(exchange);
                })
                .flatMap(chain::filter);
    }

    private static Mono<RuntimeException> exceptionFromErrorResponse(ClientResponse clientResponse, String requestUrl) {
        String errorTemplate = "Got error response [status: %s, body: %s] from authentication service %s";
        return clientResponse.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> new RuntimeException(
                        String.format(errorTemplate, clientResponse.statusCode(), body, requestUrl)
                ));
    }


}
