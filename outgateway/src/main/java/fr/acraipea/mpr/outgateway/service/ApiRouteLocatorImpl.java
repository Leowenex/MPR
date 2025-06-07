package fr.acraipea.mpr.outgateway.service;

import fr.acraipea.mpr.outgateway.config.GatewayConfiguration;
import fr.acraipea.mpr.outgateway.config.PartnerConfiguration;
import fr.acraipea.mpr.outgateway.config.ProtocolConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApiRouteLocatorImpl implements RouteLocator {

    private final RouteLocatorBuilder routeLocatorBuilder;
    private final GatewayConfiguration gatewayConfiguration;

    private static final String TARGET_PARTNER_HEADER = "X-Target-Partner";


    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routesBuilder = routeLocatorBuilder.routes();
        return Flux.fromIterable(gatewayConfiguration.getProtocols())
                .map(protocolConfiguration ->
                        protocolConfiguration.getPartnerConfigurations().stream()
                        .map(partnerConfiguration ->
                                routesBuilder.route(
                                    buildRouteId(protocolConfiguration, partnerConfiguration),
                                    predicateSpec -> buildPredicateSpec(protocolConfiguration, partnerConfiguration, predicateSpec)
                                )
                        ).toList()
                )
                .flatMap(Flux::fromIterable)
                .collectList()
                .flatMapMany(_ -> routesBuilder.build().getRoutes())
                .doOnNext(route -> log.info("Route {} created with {} filters", route.getId(), route.getFilters().size()));
    }

    private String buildRouteId(ProtocolConfiguration protocolConfiguration, PartnerConfiguration partnerConfiguration) {
        return protocolConfiguration.getProtocolName() + "-" + partnerConfiguration.getPartnerCode();
    }

    private Buildable<Route> buildPredicateSpec(ProtocolConfiguration protocolConfiguration, PartnerConfiguration partnerConfiguration, PredicateSpec predicateSpec) {
        BooleanSpec booleanSpec = predicateSpec.path(protocolConfiguration.getProtocolPath());
        booleanSpec.and().header(TARGET_PARTNER_HEADER, partnerConfiguration.getPartnerCode());
        addAuthenticationFilter(partnerConfiguration, booleanSpec);
        addRemoveTargetPartnerHeaderFilter(booleanSpec);
        return booleanSpec.uri(partnerConfiguration.getPartnerUri());
    }

    private void addAuthenticationFilter(PartnerConfiguration partnerConfiguration, BooleanSpec booleanSpec) {
        if (partnerConfiguration.getTokenAuthentication() != null) {
            booleanSpec.filters(f -> f.addRequestHeader(
                partnerConfiguration.getTokenAuthentication().getTokenHeaderName(),
                partnerConfiguration.getTokenAuthentication().getToken()
            ));
        }
        // If other authentication methods are needed, they can be added here
    }

    private void addRemoveTargetPartnerHeaderFilter(BooleanSpec booleanSpec) {
        booleanSpec.filters(f -> f.removeRequestHeader(TARGET_PARTNER_HEADER));
    }

    @Override
    public Flux<Route> getRoutesByMetadata(Map<String, Object> metadata) {
        return RouteLocator.super.getRoutesByMetadata(metadata);
    }
}
