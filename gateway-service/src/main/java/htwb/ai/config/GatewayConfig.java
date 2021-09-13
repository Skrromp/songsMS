package htwb.ai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("songs-service", r -> r.path("/songs/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://SONGS-SERVICE/songs"))

                .route("songs-service", r -> r.path("/songs/playlist/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://SONGS-SERVICE/songs/playlist"))

                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTH-SERVICE/"))
                .build();
    }
}