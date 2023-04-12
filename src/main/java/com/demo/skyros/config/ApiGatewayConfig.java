package com.demo.skyros.config;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {

        Function<PredicateSpec, Buildable<Route>> fawryRouteFunction = p -> p.path("/fawry")
                .filters(f -> f.addRequestHeader("ACCOUNT_SERVICE_CODE", "MY_FAWRY")
                        .addRequestHeader("CHANNEL", "MCC"))
                .uri("https://fawry.com");

        Function<PredicateSpec, Buildable<Route>> routeFunction = p -> p.path("/get")
                .filters(f -> f.addRequestHeader("ACCOUNT_SERVICE_CODE", "MY_FAWRY")
                        .addRequestParameter("APP_VERSION", "5.0.0")
                        .addRequestParameter("USER_NAME", "01009894481"))
                .uri("http://httpbin.org:80/");

        RouteLocator build = builder
                .routes()
                //.route(p -> p.path("/get").uri("http://httpbin.org:80/"))
                .route(p -> p.path("/currency/exchange/**", "/currency/add/**", "/currency/update/**", "/currency/delete/**", "/currency/findAll/**", "/currency/find/**").uri("lb://currency-exchange-service"))
                .route(p -> p.path("/admin/clearCache/**", "/admin/clearCacheByName/**").uri("lb://currency-exchange-service"))
                //.route(p -> p.path("/currency-exchange-app/**").filters(f -> f.rewritePath("/currency-exchange-app/(?<segment>.*)", "/currency-exchange/${segment}")).uri("lb://currency-exchange-service"))
                //.route(p -> p.path("/currency/conversion/**").uri("lb://currency-conversion-service"))
                .build();
        return build;
    }
}
