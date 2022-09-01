package com.example.voliitalia.servizi;

import com.example.voliitalia.entità.Biglietto;
import com.example.voliitalia.repositories.RepositoryBiglietto;
import com.example.voliitalia.utilità.Pagina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServizioBiglietto {

    @Autowired
    private RepositoryBiglietto repositoryBiglietto;

    public Pagina getBigliettiUtente(String username,int page,int size){
        Pageable paging= PageRequest.of(page,size);
        List<Biglietto> biglietti=new ArrayList<>();
        Page<Biglietto> risultato=repositoryBiglietto.findByUserUsername(username,paging);
        if(risultato.hasContent())
            biglietti.addAll(risultato.getContent());
        return new Pagina(biglietti, risultato.getTotalPages());
    }

    public Pagina getBigliettiUtenteScaduti(String username,int page,int size){
        Pageable paging= PageRequest.of(page,size);
        List<Biglietto> biglietti=new ArrayList<>();
        Page<Biglietto> risultato=repositoryBiglietto.findByUserUsernameScaduti(username,paging);
        if(risultato.hasContent())
            biglietti.addAll(risultato.getContent());
        return new Pagina(biglietti, risultato.getTotalPages());
    }
}
