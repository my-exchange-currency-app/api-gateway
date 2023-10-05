package com.demo.skyros.security.exception;

public class InvalidOtpException extends UnauthorizedUserException {

    public InvalidOtpException() {
        super("invalid OTP");
    }

}
