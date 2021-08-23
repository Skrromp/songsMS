package htwb.ai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("songs-service", r -> r.path("/songs/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://SONGS/songs"))

                .route("auth-service", r -> r.path("/songs/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://SONGS/songsLists"))

                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTH-SERVICE/auth-service"))
                .build();
    }

}