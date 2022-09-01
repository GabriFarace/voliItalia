package com.example.voliitalia.eccezioni;

public class ScadenzaPrenotazioneException extends RuntimeException{
    public ScadenzaPrenotazioneException(String messaggio){
        super(messaggio);
    }
}
