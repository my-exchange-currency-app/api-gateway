package com.demo.skyros.vo;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

public class AppResponse implements Serializable {
    private Date responseDate;
    private HttpStatus httpStatus;
    private String message;
    private String details;
    private Object data;

    public AppResponse() {
    }

    public AppResponse(String message) {
        this.message = message;
    }

    public AppResponse(Date responseDate, String message, String details) {
        this.responseDate = responseDate;
        this.message = message;
        this.details = details;
    }

    public AppResponse(Date responseDate, HttpStatus httpStatus, String message, String details) {
        this.responseDate = responseDate;
        this.httpStatus = httpStatus;
        this.message = message;
        this.details = details;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
