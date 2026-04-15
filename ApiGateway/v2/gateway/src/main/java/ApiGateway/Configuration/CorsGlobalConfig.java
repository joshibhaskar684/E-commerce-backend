package ApiGateway.Configuration;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsGlobalConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route /auth/** to AUTH-SERVICE
                .route("auth-service-route", r -> r.path("/auth/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("products-service", r -> r.path("/products/**")
                        .uri("lb://PRODUCTS-SERVICE"))// Eureka + load-balanced
                .build();
    }
    @Bean
    public GlobalFilter forwardAuthHeader() {
        return (exchange, chain) -> {
            String auth = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (auth != null) {
                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header("Authorization", auth)
                        .build();

                return chain.filter(exchange.mutate().request(request).build());
            }

            return chain.filter(exchange);
        };
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // frontend origin
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Set-Cookie");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}