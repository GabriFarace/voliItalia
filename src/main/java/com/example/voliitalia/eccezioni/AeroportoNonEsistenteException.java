package com.example.voliitalia.eccezioni;

public class AeroportoNonEsistenteException extends RuntimeException{
    public AeroportoNonEsistenteException(String messaggio){
        super(messaggio);
    }
}
