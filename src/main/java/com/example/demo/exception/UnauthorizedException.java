package com.example.demo.exception;


public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("User ID from request don't matches with User ID from jwt.");
    }
}
