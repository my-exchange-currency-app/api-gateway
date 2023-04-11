package com.demo.skyros.filter;

import com.demo.skyros.proxy.ClientRequestProxy;
import com.demo.skyros.vo.RequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

public class RequestLoggingDecorator extends ServerHttpRequestDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingDecorator.class);
    private static final String REQUEST_ID = "REQUEST_ID";
    private ServerHttpRequest request;
    @Value("${save.client.request}")
    private boolean saveClientRequest;
    @Autowired
    private ClientRequestProxy clientRequestProxy;

    public RequestLoggingDecorator(ServerHttpRequest delegate) {
        super(delegate);
        request = delegate;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return super.getBody().doOnNext(dataBuffer -> {
            try {
                Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                String body = new String(baos.toByteArray(), StandardCharsets.UTF_8);
                saveClientRequest(body);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void saveClientRequest(String requestBody) {
        if (saveClientRequest) {
            String tag = null;
            String requestPath = getRequest().getURI().getPath();
            if (requestPath.contains("conversion")) {
                tag = "conversion";
            }
            if (requestPath.contains("exchange")) {
                tag = "exchange";
            }
            String requestId = getRequest().getHeaders().get(REQUEST_ID) != null && !getRequest().getHeaders().get(REQUEST_ID).isEmpty() ? getRequest().getHeaders().get(REQUEST_ID).get(0) : null;
            RequestVO requestVO = new RequestVO();
            requestVO.setRequestId(requestId);
            requestVO.setRequestBody(requestBody);
            requestVO.setTag(tag);
            clientRequestProxy.saveClientRequest(requestVO);
        }
    }

    public ServerHttpRequest getRequest() {
        return request;
    }

    public void setRequest(ServerHttpRequest request) {
        this.request = request;
    }

    public ClientRequestProxy getClientRequestProxy() {
        return clientRequestProxy;
    }

    public void setClientRequestProxy(ClientRequestProxy clientRequestProxy) {
        this.clientRequestProxy = clientRequestProxy;
    }
}
