package com.example.voliitalia.eccezioni;

public class DataSpecificataNonAmmissibileException extends RuntimeException{
    public DataSpecificataNonAmmissibileException(String messaggio){
        super(messaggio);
    }
}
