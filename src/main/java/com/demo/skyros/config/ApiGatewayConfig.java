package com.demo.skyros.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        RouteLocator build = builder
                .routes()
                .route(p -> p.path("/get")
                        .filters(f -> f.addRequestHeader("ACCOUNT_SERVICE_CODE", "MY_FAWRY"))
                        .uri("http://httpbin.org:80/"))
                .build();
        return build;
    }
}
