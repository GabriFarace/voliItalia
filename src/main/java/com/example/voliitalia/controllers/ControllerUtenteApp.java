package com.example.voliitalia.controllers;


import com.example.voliitalia.eccezioni.NomeCompagniaEsistenteException;
import com.example.voliitalia.eccezioni.UsernameEsistenteException;
import com.example.voliitalia.entità.CompagniaAerea;
import com.example.voliitalia.entità.Utente;
import com.example.voliitalia.servizi.ServizioUtenteCompagnia;
import com.example.voliitalia.sicurezza.CompagniaPrincipal;
import com.example.voliitalia.sicurezza.TokenProvider;
import com.example.voliitalia.sicurezza.UtentePrincipal;
import com.example.voliitalia.utilità.RispostaHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/appVoli")
public class ControllerUtenteApp {

    @Autowired
    private ServizioUtenteCompagnia servizioUtenteCompagnia;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity login(@RequestParam("username") String username,@RequestParam("password") String password){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            Utente utente = servizioUtenteCompagnia.ottieniUtente(username);
            HttpHeaders headers = new HttpHeaders();
            if(utente != null) {
                headers.add("Jwt-Token", tokenProvider.generateJwtTokenUser(new UtentePrincipal(utente)));
                return new ResponseEntity<>(utente, headers, HttpStatus.OK);
            }else{
                CompagniaAerea compagniaAerea = servizioUtenteCompagnia.ottieniCompagnia(username);
                headers.add("Jwt-Token",tokenProvider.generateJwtTokenAirline(new CompagniaPrincipal(compagniaAerea)));
                return new ResponseEntity<>(compagniaAerea,headers,HttpStatus.OK);
            }
        }catch(UsernameNotFoundException unfe){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST, unfe.getMessage());
        }catch(BadCredentialsException bce){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,"Username/Password errata");
        }catch(Exception e){
            System.out.println(e);
            return creaRispostaHttp(HttpStatus.INTERNAL_SERVER_ERROR,"Errore nel processare la richiesta");
        }

    }


    @PostMapping("/registrazioneUtente")
    public ResponseEntity registrazioneUtente(@RequestBody Utente utente){
        try{
            Utente nuovoUtente=servizioUtenteCompagnia.registrazioneUtente(utente.getUsername(), utente.getPassword(), utente.getNome(), utente.getCognome());
            return new ResponseEntity<>(nuovoUtente,HttpStatus.OK);
        }catch(UsernameEsistenteException uee){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,uee.getMessage());
        }catch(Exception e){
            System.out.println(e);
        return creaRispostaHttp(HttpStatus.INTERNAL_SERVER_ERROR,"Errore nel processare la richiesta");
        }
    }

    @PostMapping("/registrazioneCompagniaAerea")
    public ResponseEntity registrazioneCompagniaAerea(@RequestBody CompagniaAerea compagniaAerea){
        try{
            CompagniaAerea nuovaCompagnia=servizioUtenteCompagnia.registrazioneCompagnia(compagniaAerea.getUsername(), compagniaAerea.getPassword(), compagniaAerea.getNome(), compagniaAerea.getNazione(), compagniaAerea.getSede());
            return new ResponseEntity<>(nuovaCompagnia,HttpStatus.OK);
        }catch(UsernameEsistenteException uee){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,uee.getMessage());
        }catch(NomeCompagniaEsistenteException ncee){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST, ncee.getMessage());
        }catch(Exception e){
            return creaRispostaHttp(HttpStatus.INTERNAL_SERVER_ERROR,"Errore nel processare la richiesta");
        }
    }

    public static ResponseEntity<RispostaHttp> creaRispostaHttp(HttpStatus httpStatus, String messaggio) {
        return new ResponseEntity<>(new RispostaHttp(httpStatus.value(), httpStatus, messaggio), httpStatus);
    }



}
