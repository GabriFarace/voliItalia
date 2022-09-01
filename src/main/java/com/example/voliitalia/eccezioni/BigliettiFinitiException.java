package com.example.voliitalia.eccezioni;

public class BigliettiFinitiException extends RuntimeException{
    public BigliettiFinitiException(String messaggio){
        super(messaggio);
    }
}
