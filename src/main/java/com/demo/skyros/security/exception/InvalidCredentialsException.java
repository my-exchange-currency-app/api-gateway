package com.demo.skyros.security.exception;

public class InvalidCredentialsException extends UnauthorizedUserException {

    public InvalidCredentialsException() {
        super();
    }

    public InvalidCredentialsException(String msg) {
        super(msg);
    }

}
