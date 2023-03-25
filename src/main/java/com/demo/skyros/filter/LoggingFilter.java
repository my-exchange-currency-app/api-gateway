package com.demo.skyros.filter;

import com.demo.skyros.proxy.ClientRequestProxy;
import com.demo.skyros.vo.CurrencyVO;
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

import java.math.BigDecimal;

@Component
public class LoggingFilter implements GlobalFilter {

    private static final String REQUEST_ID = "REQUEST_ID";
    Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    @Value("${save.client.request}")
    private boolean saveClientRequest;
    @Autowired
    private ClientRequestProxy clientRequestProxy;

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
        String tag = null;
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();
        if (requestPath.contains("currency-conversion")) {
            tag = "conversion";
        }
        if (requestPath.contains("currency-exchange")) {
            tag = "exchange";
        }
        String requestId = request.getHeaders().get(REQUEST_ID) != null && !request.getHeaders().get(REQUEST_ID).isEmpty() ? request.getHeaders().get(REQUEST_ID).get(0) : null;
        CurrencyVO vo = prepareCurrencyVO(requestPath.split("/"));
        vo.setTag(tag);
        clientRequestProxy.saveClientRequest(requestId, vo);
    }

    private CurrencyVO prepareCurrencyVO(String[] pathList) {
        CurrencyVO currencyExchangeVO = new CurrencyVO();
        for (int i = 0; i < pathList.length; i++) {
            if (pathList[i].equals("from")) {
                String from = pathList[i + 1];
                currencyExchangeVO.setFrom(from);
            }
            if (pathList[i].equals("to")) {
                String to = pathList[i + 1];
                currencyExchangeVO.setTo(to);
            }
            if (pathList[i].equals("quantity")) {
                String quantity = pathList[i + 1];
                currencyExchangeVO.setQuantity(new BigDecimal(quantity));
            }
        }
        return currencyExchangeVO;
    }

}
