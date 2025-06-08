package fr.acraipea.mpr.outgateway.service;

import fr.acraipea.mpr.outgateway.config.GatewayConfiguration;
import fr.acraipea.mpr.outgateway.config.PartnerConfiguration;
import fr.acraipea.mpr.outgateway.config.ProtocolConfiguration;
import fr.acraipea.mpr.outgateway.filters.auth.BearerTokenAuthentificationFilter;
import fr.acraipea.mpr.outgateway.filters.auth.PreSharedTokenAuthentificationFilter;
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
import java.util.Optional;

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
                        protocolConfiguration.partnerConfigurations().stream()
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
        return protocolConfiguration.protocolName() + "-" + partnerConfiguration.partnerCode();
    }

    private Buildable<Route> buildPredicateSpec(ProtocolConfiguration protocolConfiguration, PartnerConfiguration partnerConfiguration, PredicateSpec predicateSpec) {
        BooleanSpec booleanSpec = predicateSpec.path(protocolConfiguration.protocolPath()); // Ensure the path matches the protocol path
        booleanSpec.and().header(TARGET_PARTNER_HEADER, partnerConfiguration.partnerCode()); // Ensure the X-Target-Partner header matches the partner code
        addAuthenticationFilter(partnerConfiguration, booleanSpec); // Add authentication filters if configured
        addRemoveTargetPartnerHeaderFilter(booleanSpec); // Remove the X-Target-Partner header from the request
        return booleanSpec.uri(partnerConfiguration.partnerUri());
    }

    private void addAuthenticationFilter(PartnerConfiguration partnerConfiguration, BooleanSpec booleanSpec) {

        Optional.ofNullable(partnerConfiguration.preSharedTokenAuthentification()).ifPresent(preSharedTokenAuthentification ->
            booleanSpec.filters(f -> f.filter(new PreSharedTokenAuthentificationFilter(preSharedTokenAuthentification))));

        Optional.ofNullable(partnerConfiguration.bearerTokenAuthentication()).ifPresent(bearerTokenAuthentication ->
            booleanSpec.filters(f -> f.filter(new BearerTokenAuthentificationFilter(bearerTokenAuthentication))));

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
