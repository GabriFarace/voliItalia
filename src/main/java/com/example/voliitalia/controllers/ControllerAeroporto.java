package com.example.voliitalia.controllers;

import com.example.voliitalia.eccezioni.AeroportoNonEsistenteException;
import com.example.voliitalia.entità.Aeroporto;
import com.example.voliitalia.servizi.ServizioAeroporto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.voliitalia.controllers.ControllerUtenteApp.creaRispostaHttp;

@RestController
@RequestMapping( path = "/aeroporti")
public class ControllerAeroporto {

    @Autowired
    private ServizioAeroporto servizioAeroporto;

    @GetMapping
    public ResponseEntity ottieniAeroporti(){
        List<Aeroporto> aeroporti=servizioAeroporto.getAeroporti();
        if(aeroporti.isEmpty())
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,"Nessun aeroporto presente nel database");
        return new ResponseEntity(aeroporti,HttpStatus.OK);
    }
}
