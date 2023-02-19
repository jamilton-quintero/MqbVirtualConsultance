package com.example.analisis.polly.exceptions;

public class AwsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AwsException(String message) {
        super(message);
    }

}
