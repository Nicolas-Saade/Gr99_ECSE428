package com.mcgill.ecse428.textbook_exchange.exception;

import org.springframework.http.HttpStatus;

import io.micrometer.common.lang.NonNull;

public class TextBookExchangeException extends RuntimeException {
    @NonNull
    private HttpStatus status;
    
    public TextBookExchangeException(HttpStatus status,String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
    
}
