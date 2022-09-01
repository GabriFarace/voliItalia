package com.example.voliitalia.servizi;

import com.example.voliitalia.eccezioni.NomeCompagniaEsistenteException;
import com.example.voliitalia.eccezioni.UsernameEsistenteException;
import com.example.voliitalia.entità.CompagniaAerea;
import com.example.voliitalia.entità.Utente;
import com.example.voliitalia.repositories.RepositoryCompagnia;
import com.example.voliitalia.repositories.RepositoryUtente;
import com.example.voliitalia.sicurezza.CompagniaPrincipal;
import com.example.voliitalia.sicurezza.UtentePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Qualifier("userDetailsService")
public class ServizioUtenteCompagnia implements UserDetailsService {

    @Autowired
    private RepositoryCompagnia repositoryCompagnia;
    @Autowired
    private RepositoryUtente repositoryUtente;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente=repositoryUtente.findByUsername(username);
        if(utente==null){
            CompagniaAerea compagniaAerea=repositoryCompagnia.findByUsername(username);
            if(compagniaAerea == null)
                throw new UsernameNotFoundException("Utente o Compagnia con username:"+username+" non trovato");
            return new CompagniaPrincipal(compagniaAerea);
        }
        UtentePrincipal principal=new UtentePrincipal(utente);
        return principal;
    }

    public CompagniaAerea registrazioneCompagnia(String username,String password,String nome,String nazione,String sede){
        controllaUsernameEsistente(username);
        CompagniaAerea compagnia=repositoryCompagnia.findByNome(nome);
        if(compagnia != null)
            throw new NomeCompagniaEsistenteException("Il nome specificato per la Compagnia è stato già assegnato ad un'altra compagnia");
        CompagniaAerea nuovaCompagnia=new CompagniaAerea();
        nuovaCompagnia.setUsername(username);
        nuovaCompagnia.setPassword(passwordEncoder.encode(password));
        //nuovaCompagnia.setPassword(password);
        nuovaCompagnia.setNome(nome);
        nuovaCompagnia.setNazione(nazione);
        nuovaCompagnia.setSede(sede);
        repositoryCompagnia.save(nuovaCompagnia);
        return nuovaCompagnia;
    }

    public Utente registrazioneUtente(String username,String password,String nome,String cognome){
        controllaUsernameEsistente(username);
        Utente nuovoUtente=new Utente();
        nuovoUtente.setUsername(username);
        nuovoUtente.setPassword(passwordEncoder.encode(password));
        //nuovoUtente.setPassword(password);
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        repositoryUtente.save(nuovoUtente);
        return nuovoUtente;
    }

    public Utente ottieniUtente(String username){
        return repositoryUtente.findByUsername(username);
    }

    public CompagniaAerea ottieniCompagnia(String username){
        return repositoryCompagnia.findByUsername(username);
    }



    private void controllaUsernameEsistente(String username) {
        Utente utente=repositoryUtente.findByUsername(username);
        if(utente != null)
            throw new UsernameEsistenteException("Lo username specificato non è disponibile perchè è stato già assegnato");
        CompagniaAerea compagnia=repositoryCompagnia.findByUsername(username);
        if(compagnia != null)
            throw new UsernameEsistenteException("Lo username specificato non è disponibile perchè è stato già assegnato");
    }


}
