package com.example.voliitalia.controllers;


import com.example.voliitalia.eccezioni.*;
import com.example.voliitalia.entità.CompagniaAerea;
import com.example.voliitalia.entità.Volo;
import com.example.voliitalia.servizi.ServizioVolo;
import com.example.voliitalia.utilità.Pagina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.voliitalia.controllers.ControllerUtenteApp.creaRispostaHttp;


@RestController
@RequestMapping("/voli")
public class ControllerVolo {

    @Autowired
    private ServizioVolo servizioVolo;



    @PostMapping("/diretti/{page}/{size}")
    public ResponseEntity ottieniVoliDiretti(@RequestBody Volo dettagliVolo,
                                             @PathVariable(value = "page")int page,
                                             @PathVariable(value = "size")int size) throws InterruptedException {
        Pagina voli = servizioVolo.getVoliDiretti(dettagliVolo,page,size);
        System.out.println(voli.getElementi()+" pagine:"+voli.getNumPagine());
        return new ResponseEntity<>(voli,HttpStatus.OK);
    }


    @PostMapping("/conScalo/{page}/{size}")
    public ResponseEntity ottieniVoliConScalo(@RequestBody Volo dettagliVolo,
                                              @PathVariable(value = "page")int page,
                                              @PathVariable(value = "size")int size){

        if(dettagliVolo.getAeroportoPartenza().getCitta() == null || dettagliVolo.getAeroportoDestinazione().getCitta() == null)
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,"Specificare citta di partenza e di destinazione per ottenere i voli con scalo");
        Pagina voli = servizioVolo.getVoliSingoloScalo(dettagliVolo,page,size);
        return new ResponseEntity<>(voli,HttpStatus.OK);
    }

    @PostMapping("/conDoppioScalo/{page}/{size}")
    public ResponseEntity ottieniVoliConDoppioScalo(@RequestBody Volo dettagliVolo,
                                                    @PathVariable(value = "page")int page,
                                                    @PathVariable(value = "size")int size){

        if(dettagliVolo.getAeroportoPartenza().getCitta() == null || dettagliVolo.getAeroportoDestinazione().getCitta() == null)
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,"Specificare citta di partenza e di destinazione per ottenere i voli con scalo");
        Pagina voli = servizioVolo.getVoliDoppioScalo(dettagliVolo,page,size);
        return new ResponseEntity<>(voli,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('airline')")
    @GetMapping("/compagnia/nonPartiti")
    public ResponseEntity ottieniVoliNonPartitiCompagnia(@RequestParam(value = "page",defaultValue = "0") int page,@RequestParam(value = "size",defaultValue = "10") int size){
        Pagina voli=servizioVolo.getVoliNonPartitiCompagnia((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal(),page,size);
        return new ResponseEntity<>(voli,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('airline')")
    @GetMapping("/compagnia/partiti")
    public ResponseEntity ottieniVoliPartitiCompagnia(@RequestParam(value = "page",defaultValue = "0") int page,@RequestParam(value = "size",defaultValue = "10") int size){
        Pagina voli=servizioVolo.getVoliPartitiCompagnia((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal(),page,size);
        return new ResponseEntity<>(voli,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('airline')")
    @PostMapping("/aggiungiVolo")
    public ResponseEntity aggiungiVolo(@RequestBody Volo volo){
        if(volo.getAeroportoPartenza()==null || volo.getAeroportoDestinazione()== null || volo.getAeroportoPartenza().getNome()==null || volo.getAeroportoDestinazione().getNome()==null ||
           volo.getScadenzaPrenotazione() == null || volo.getDataPartenza() == null || volo.getDataDestinazione()==null ||
           volo.getNumPosti()==null || volo.getPrezzoBiglietto()==null)
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,"Devi specificare tutti i campi per creare un volo");
        try{
            CompagniaAerea compagniaAerea=new CompagniaAerea();
            compagniaAerea.setUsername((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            volo.setCompagniaAerea(compagniaAerea);
            Volo nuovoVolo=servizioVolo.addVolo(volo,(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return new ResponseEntity(nuovoVolo,HttpStatus.CREATED);
        }catch(DataSpecificataNonAmmissibileException dsnae){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,dsnae.getMessage());
        }catch(AeroportoNonEsistenteException anee){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST, anee.getMessage());
        }

    }


    @PreAuthorize("hasAuthority('airline')")
    @PutMapping("/modificaVolo")
    public ResponseEntity modificaVolo(@RequestBody Volo v){
        if(v.getId() == null)
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,"Specificare l'id del volo che si vuole modificare");
        try{
            Volo nuovoVolo=servizioVolo.modificaPrezzoBigliettoVolo(v,(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return new ResponseEntity(nuovoVolo,HttpStatus.OK);
        }catch(OperazioneNonAutorizzataException onae){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST,onae.getMessage());
        }catch(VoloNonEsistenteException vnee){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST, vnee.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/prenota")
    public ResponseEntity prenota(@RequestBody List<Volo> voli){
        try{
            if(voli.isEmpty())
                return creaRispostaHttp(HttpStatus.BAD_REQUEST,"Inserire i voli che si vogliono prenotare");
            List<Volo> voliPrenotati=servizioVolo.prenota(voli,(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return new ResponseEntity(voliPrenotati,HttpStatus.OK);
        }catch(ScadenzaPrenotazioneException spe){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST, spe.getMessage());
        }catch(VoloNonEsistenteException vnee) {
            return creaRispostaHttp(HttpStatus.BAD_REQUEST, vnee.getMessage());
        }catch(BigliettiFinitiException bfe){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST, bfe.getMessage());
        }catch(PrezzoCambiatoException pce){
            return creaRispostaHttp(HttpStatus.BAD_REQUEST, pce.getMessage());
        }

    }
}
