package com.demo.skyros.filter;

import com.demo.skyros.service.ClientRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {

    private static final String REQUEST_ID = "REQUEST_ID";
    Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    @Value("${save.client.request}")
    private boolean saveClientRequest;
    @Autowired
    private ClientRequestService clientRequestService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            if (saveClientRequest) {
                saveClientRequest(exchange);
            }
        } catch (Exception ex) {
            logger.error("failed to save Client request -> {}", ex.getMessage());
        }
        logger.info("Request Path -> {}", exchange.getRequest().getPath());
        return chain.filter(exchange);
    }


    private void saveClientRequest(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();
        String requestId = request.getHeaders().get(REQUEST_ID) != null && !request.getHeaders().get(REQUEST_ID).isEmpty() ? request.getHeaders().get(REQUEST_ID).get(0) : null;
        getClientRequestService().saveClientRequest(requestPath, requestId);
    }

    public ClientRequestService getClientRequestService() {
        return clientRequestService;
    }

    public void setClientRequestService(ClientRequestService clientRequestService) {
        this.clientRequestService = clientRequestService;
    }
}
