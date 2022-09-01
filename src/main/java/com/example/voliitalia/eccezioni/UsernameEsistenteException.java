package com.example.voliitalia.eccezioni;

public class UsernameEsistenteException extends RuntimeException{
    public UsernameEsistenteException(String messaggio){
        super(messaggio);
    }
}
