package com.demo.skyros.filter;

import com.demo.skyros.proxy.AuthProxy;
import com.demo.skyros.vo.AppResponse;
import com.demo.skyros.vo.TokenVO;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@Log4j2
public class AuthFilter implements GlobalFilter {

    @Autowired
    private AuthProxy authProxy;
    @Value("${auth.skip.urls}")
    private String[] skipAuthUrls;
    @Value("${validate.token.url}")
    private String validateTokenUrl;
    @Autowired
    private RestTemplate restTemplate;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();

        if (Arrays.asList(getSkipAuthUrls()).contains(url)) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        //AppResponse appResponse = getAuthProxy().validateToken(new TokenVO(token));
        boolean validateToken = validateToken(new TokenVO(token));
        log.info("token status= " + validateToken);
        if (!validateToken) {

        }

        return chain.filter(exchange);
    }

    private boolean validateToken(TokenVO tokenVO) {
        AppResponse appResponse = restTemplate.postForObject(getValidateTokenUrl(), tokenVO, AppResponse.class);
        return (boolean) appResponse.getData();
    }

    public AuthProxy getAuthProxy() {
        return authProxy;
    }

    public void setAuthProxy(AuthProxy authProxy) {
        this.authProxy = authProxy;
    }

    public String[] getSkipAuthUrls() {
        return skipAuthUrls;
    }

    public void setSkipAuthUrls(String[] skipAuthUrls) {
        this.skipAuthUrls = skipAuthUrls;
    }

    public String getValidateTokenUrl() {
        return validateTokenUrl;
    }

    public void setValidateTokenUrl(String validateTokenUrl) {
        this.validateTokenUrl = validateTokenUrl;
    }
}
