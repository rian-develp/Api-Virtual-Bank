package com.example.virtualbank.responsebody;

import java.time.LocalDateTime;

// ResponseBody customizado

public class ResponseBody<T> {

    private final int statusCode;
    private final LocalDateTime timestamp;
    private final T data;

    public ResponseBody(int statusCode, T data) {
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    public static <T> ResponseBody<T> responseBody(int statusCode, T data){
        return new ResponseBody<>(statusCode, data);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }
}
