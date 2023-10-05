package com.demo.skyros.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ForceChangePasswordException extends RuntimeException {

    public ForceChangePasswordException() {
        super();
    }

    public ForceChangePasswordException(String msg) {
        super(msg);
    }

}
