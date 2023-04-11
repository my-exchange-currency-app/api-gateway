package com.demo.skyros.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerWebExchangeDecorator decorator =
                new ServerWebExchangeDecorator(serverWebExchange) {
                    @Override
                    public ServerHttpRequest getRequest() {
                        return new RequestLoggingDecorator(serverWebExchange.getRequest());
                    }
                };

        return webFilterChain.filter(decorator);
    }

}
