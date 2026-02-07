package com.javaapp.backend_7irfati.exception;

public class InvalidToken extends RuntimeException{
    public InvalidToken (String message){
        super(message);
    }
}
