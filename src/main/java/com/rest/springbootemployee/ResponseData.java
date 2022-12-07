package com.rest.springbootemployee;

public class ResponseData {
    private String Status;
    private String message;

    public ResponseData(String status, String message) {
        Status = status;
        this.message = message;
    }
}
