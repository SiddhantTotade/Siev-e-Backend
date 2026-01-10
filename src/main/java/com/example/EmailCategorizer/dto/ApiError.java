package com.example.EmailCategorizer.dto;

public class ApiError {

    private int status;
    private String message;
    private String detail;

    public ApiError(int status, String message, String detail) {
        this.status = status;
        this.message = message;
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}
