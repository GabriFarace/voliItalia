package com.example.voliitalia.eccezioni;

public class VoloNonEsistenteException extends RuntimeException{
    public VoloNonEsistenteException(String messaggio){
        super(messaggio);
    }
}
