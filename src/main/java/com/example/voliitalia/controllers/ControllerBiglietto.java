package com.example.voliitalia.controllers;



import com.example.voliitalia.servizi.ServizioBiglietto;
import com.example.voliitalia.utilità.Pagina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "/biglietti")
public class ControllerBiglietto {

    @Autowired
    private ServizioBiglietto servizioBiglietto;

    @PreAuthorize("hasAuthority('user')")
    @GetMapping("/validi")
    public ResponseEntity ottieniBiglietti(@RequestParam(value = "page",defaultValue = "0") int page,@RequestParam(value = "size",defaultValue = "10") int size){
            Pagina biglietti=servizioBiglietto.getBigliettiUtente((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal(),page,size);
            return new ResponseEntity(biglietti,HttpStatus.OK);

    }


    @PreAuthorize("hasAuthority('user')")
    @GetMapping("/scaduti")
    public ResponseEntity ottieniViaggi(@RequestParam(value = "page",defaultValue = "0") int page,@RequestParam(value = "size",defaultValue = "10") int size){
            Pagina biglietti=servizioBiglietto.getBigliettiUtenteScaduti((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal(),page,size);
            return new ResponseEntity(biglietti,HttpStatus.OK);
    }
}
