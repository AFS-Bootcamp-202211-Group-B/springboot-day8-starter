package com.rest.springbootemployee;

public class ResponseData {
    private int status;
    private String message;
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
