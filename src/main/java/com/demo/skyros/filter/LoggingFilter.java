package com.demo.skyros.filter;

import com.demo.skyros.entity.ClientRequestEntity;
import com.demo.skyros.entity.EntityAudit;
import com.demo.skyros.repo.ClientRequestRepo;
import com.demo.skyros.vo.CurrencyExchangeVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class LoggingFilter implements GlobalFilter {

    private static final String REQUEST_ID = "REQUEST_ID";
    Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    @Autowired
    private ClientRequestRepo clientRequestRepo;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            saveClientRequest(exchange);
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
        if (null != requestId) {
            String[] pathList = requestPath.split("/");
            CurrencyExchangeVO currencyExchangeVO = prepareCurrencyExchangeVO(pathList);
            ClientRequestEntity clientRequest = new ClientRequestEntity();

            if (requestPath.contains("currency-conversion")) {
                clientRequest.setTag("conversion");
            }
            if (requestPath.contains("currency-exchange")) {
                clientRequest.setTag("exchange");
            }

            clientRequest.setRequestId(requestId);
            clientRequest.setRequestBody(gson.toJson(currencyExchangeVO));
            clientRequest.setAudit(prepareAudit());
            getClientRequestRepo().save(clientRequest);
        }
    }

    CurrencyExchangeVO prepareCurrencyExchangeVO(String[] pathList) {

        CurrencyExchangeVO currencyExchangeVO = new CurrencyExchangeVO();
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

    private EntityAudit prepareAudit() {
        EntityAudit audit = new EntityAudit();
        audit.setCreatedBy("system");
        audit.setCreatedDate(new Date());
        audit.setLastModifiedBy("system");
        audit.setLastModifiedDate(new Date());
        return audit;
    }

    public ClientRequestRepo getClientRequestRepo() {
        return clientRequestRepo;
    }

    public void setClientRequestRepo(ClientRequestRepo clientRequestRepo) {
        this.clientRequestRepo = clientRequestRepo;
    }
}
