package com.demo.skyros.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
public class OtpRequiredException extends RuntimeException {

    public OtpRequiredException() {
        super();
    }

    public OtpRequiredException(String msg) {
        super(msg);
    }

}
