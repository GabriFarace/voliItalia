package com.example.voliitalia.eccezioni;

public class NomeCompagniaEsistenteException extends RuntimeException{
    public NomeCompagniaEsistenteException(String messaggio){
        super(messaggio);
    }
}
