package com.demo.skyros.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

public class RequestLoggingDecorator extends ServerHttpRequestDecorator {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingDecorator.class);
    private static final String REQUEST_ID = "REQUEST_ID";
    private ServerHttpRequest request;

    public RequestLoggingDecorator(ServerHttpRequest delegate) {
        super(delegate);
        request = delegate;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(dataBuffer -> {
            logClientRequest(dataBuffer);
        });
    }


    private void logClientRequest(DataBuffer dataBuffer) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
            String body = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            log.info("request path >> {}", getRequest().getPath());
            log.info("requestBody >> {}", body);
            String requestId = getRequest().getHeaders().get(REQUEST_ID) != null && !getRequest().getHeaders().get(REQUEST_ID).isEmpty() ? getRequest().getHeaders().get(REQUEST_ID).get(0) : null;
            log.info("requestId >> {}", requestId);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ServerHttpRequest getRequest() {
        return request;
    }

    public void setRequest(ServerHttpRequest request) {
        this.request = request;
    }

}
