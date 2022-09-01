package com.example.voliitalia.eccezioni;

public class OperazioneNonAutorizzataException extends RuntimeException{
    public OperazioneNonAutorizzataException(String messaggio){
        super(messaggio);
    }
}
