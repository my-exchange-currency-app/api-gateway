package com.demo.skyros.security.exception;


public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("user not found with Id : " + id);
    }

    public UserNotFoundException(String userName) {
        super("user not found with : " + userName);
    }

}
