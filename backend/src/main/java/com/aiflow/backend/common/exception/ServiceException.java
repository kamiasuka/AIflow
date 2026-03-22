package com.aiflow.backend.common.exception;


import com.aiflow.backend.common.response.StatusCode;
import lombok.Getter;

public class ServiceException extends RuntimeException{
    @Getter
    private StatusCode statusCode;

    public ServiceException(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public ServiceException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
