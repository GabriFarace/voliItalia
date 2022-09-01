package com.example.voliitalia.eccezioni;

public class PrezzoCambiatoException extends RuntimeException{
    public PrezzoCambiatoException(String messaggio){
        super(messaggio);
    }
}
