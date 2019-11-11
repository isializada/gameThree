package com.alizada.exception;

public class RabbitMqException extends RuntimeException {
    static final long serialVersionUID = 6602049037777967231L;

    public RabbitMqException(String message, Throwable cause) {
        super(message, cause);
    }
}
