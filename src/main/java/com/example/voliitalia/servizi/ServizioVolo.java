package com.example.voliitalia.servizi;

import com.example.voliitalia.eccezioni.*;
import com.example.voliitalia.entità.*;
import com.example.voliitalia.repositories.*;
import com.example.voliitalia.utilità.Pagina;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServizioVolo {

    @Autowired
    private RepositoryVolo repositoryVolo;

    @Autowired
    private RepositoryBiglietto repositoryBiglietto;

    @Autowired
    private RepositoryAeroporto repositoryAeroporto;

    @Autowired
    private RepositoryCompagnia repositoryCompagnia;

    @Autowired
    private RepositoryUtente repositoryUtente;

    public Pagina getVoliDiretti(Volo dettagliVolo, int page, int size){

        Pageable paging= PageRequest.of(page,size);
        List<List<Volo>> voli = new ArrayList<>();

        Calendar dataPar = dettagliVolo.getDataPartenza()!=null? dettagliVolo.getDataPartenza():Calendar.getInstance();
        Calendar dataPartenza=DateUtils.truncate(dataPar,Calendar.DAY_OF_MONTH);
        Calendar dataGiornoDopo=DateUtils.ceiling(dataPar,Calendar.DAY_OF_MONTH);
        if(DateUtils.isSameDay(dataPar,Calendar.getInstance()))
            dataPartenza=Calendar.getInstance();

        String cittaAeroportoPartenza=dettagliVolo.getAeroportoPartenza().getCitta()!=null? dettagliVolo.getAeroportoPartenza().getCitta():"";
        String cittaAeroportoDestinazione=dettagliVolo.getAeroportoDestinazione().getCitta()!=null? dettagliVolo.getAeroportoDestinazione().getCitta():"";
        String nomeCompagniaAerea=dettagliVolo.getCompagniaAerea().getNome()!=null?dettagliVolo.getCompagniaAerea().getNome():"";
        Double prezzoBiglietto= dettagliVolo.getPrezzoBiglietto()!=null?dettagliVolo.getPrezzoBiglietto():0.0;


        Page<Volo> risultato=repositoryVolo.ricercaAvanzataVoliDiretti(cittaAeroportoPartenza,cittaAeroportoDestinazione,dataPartenza,dataGiornoDopo,nomeCompagniaAerea,prezzoBiglietto, paging);
        if(risultato.hasContent()) {
            voli.addAll(listaVoliDiretti(risultato.getContent()));
        }
        return new Pagina(voli,risultato.getTotalPages());

    }

    public Pagina getVoliSingoloScalo(Volo dettagliVolo, int page, int size){

        Pageable paging= PageRequest.of(page,size);
        List<List<Volo>> voli = new ArrayList<>();

        Calendar dataPar = dettagliVolo.getDataPartenza()!=null? dettagliVolo.getDataPartenza():Calendar.getInstance();
        Calendar dataPartenza=DateUtils.truncate(dataPar,Calendar.DAY_OF_MONTH);
        Calendar dataGiornoDopo=DateUtils.ceiling(dataPar,Calendar.DAY_OF_MONTH);
        if(DateUtils.isSameDay(dataPar,Calendar.getInstance()))
            dataPartenza=Calendar.getInstance();

        String cittaAeroportoPartenza=dettagliVolo.getAeroportoPartenza().getCitta()!=null? dettagliVolo.getAeroportoPartenza().getCitta():"";
        String cittaAeroportoDestinazione=dettagliVolo.getAeroportoDestinazione().getCitta()!=null? dettagliVolo.getAeroportoDestinazione().getCitta():"";
        String nomeCompagniaAerea=dettagliVolo.getCompagniaAerea().getNome()!=null?dettagliVolo.getCompagniaAerea().getNome():"";
        Double prezzoBiglietto= dettagliVolo.getPrezzoBiglietto()!=null?dettagliVolo.getPrezzoBiglietto():0.0;

        Page<Object[]> risultato=repositoryVolo.ricercaAvanzataVoliConSingoloScalo(cittaAeroportoPartenza,cittaAeroportoDestinazione,dataPartenza,dataGiornoDopo,nomeCompagniaAerea, prezzoBiglietto, paging);
        if(risultato.hasContent())
            voli.addAll(listaVoliConScalo(risultato.getContent()));
        return new Pagina(voli,risultato.getTotalPages());

    }

    public Pagina getVoliDoppioScalo(Volo dettagliVolo, int page, int size){

        Pageable paging= PageRequest.of(page,size);
        List<List<Volo>> voli = new ArrayList<>();

        Calendar dataPar = dettagliVolo.getDataPartenza()!=null? dettagliVolo.getDataPartenza():Calendar.getInstance();
        Calendar dataPartenza=DateUtils.truncate(dataPar,Calendar.DAY_OF_MONTH);
        Calendar dataGiornoDopo=DateUtils.ceiling(dataPar,Calendar.DAY_OF_MONTH);
        if(DateUtils.isSameDay(dataPar,Calendar.getInstance()))
            dataPartenza=Calendar.getInstance();

        String cittaAeroportoPartenza=dettagliVolo.getAeroportoPartenza().getCitta()!=null? dettagliVolo.getAeroportoPartenza().getCitta():"";
        String cittaAeroportoDestinazione=dettagliVolo.getAeroportoDestinazione().getCitta()!=null? dettagliVolo.getAeroportoDestinazione().getCitta():"";
        String nomeCompagniaAerea=dettagliVolo.getCompagniaAerea().getNome()!=null?dettagliVolo.getCompagniaAerea().getNome():"";
        Double prezzoBiglietto= dettagliVolo.getPrezzoBiglietto()!=null?dettagliVolo.getPrezzoBiglietto():0.0;

        Page<Object[]> risultato=repositoryVolo.ricercaAvanzataVoliConDoppioScalo(cittaAeroportoPartenza,cittaAeroportoDestinazione,dataPartenza,dataGiornoDopo,nomeCompagniaAerea, prezzoBiglietto, paging);
        if(risultato.hasContent())
            voli.addAll(listaVoliConScalo(risultato.getContent()));
        return new Pagina(voli,risultato.getTotalPages());

    }

    public Pagina getVoliNonPartitiCompagnia(String username,int page,int size){
        Pageable paging=PageRequest.of(page,size);
        List<Volo> voli=new ArrayList<>();
        Page<Volo> risultato=repositoryVolo.findVoliNonPartitiCompagnia(username,paging);
        if(risultato.hasContent())
            voli.addAll(risultato.getContent());
        return new Pagina(voli, risultato.getTotalPages());
    }

    public Pagina getVoliPartitiCompagnia(String username,int page,int size){
        Pageable paging=PageRequest.of(page,size);
        List<Volo> voli=new ArrayList<>();
        Page<Volo> risultato=repositoryVolo.findVoliPartitiCompagnia(username,paging);
        if(risultato.hasContent())
            voli.addAll(risultato.getContent());
        return new Pagina(voli, risultato.getTotalPages());
    }

    public Volo addVolo(Volo volo,String usernameCompagnia){

        CompagniaAerea compagnia=repositoryCompagnia.findByUsername(usernameCompagnia);

        if(volo.getScadenzaPrenotazione().before(Calendar.getInstance()))
            throw new DataSpecificataNonAmmissibileException("La data di scadenza non può venire prima della data attuale");
        if(volo.getDataPartenza().before(Calendar.getInstance()))
            throw new DataSpecificataNonAmmissibileException("La data di scadenza per le prenotazioni deve venire prima della data di partenza");

        Aeroporto partenza = repositoryAeroporto.findByNome(volo.getAeroportoPartenza().getNome());
        if(partenza == null)
            throw new AeroportoNonEsistenteException("L'aeroporto di partenza specificato non esiste");

        Aeroporto destinazione = repositoryAeroporto.findByNome(volo.getAeroportoDestinazione().getNome());
        if(destinazione == null)
            throw new AeroportoNonEsistenteException("L'aeroporto di destinazione specificato non esiste");


        Volo nuovoVolo=new Volo();
        nuovoVolo.setAeroportoPartenza(partenza);
        nuovoVolo.setAeroportoDestinazione(destinazione);
        nuovoVolo.setCompagniaAerea(compagnia);
        nuovoVolo.setDataPartenza(volo.getDataPartenza());
        nuovoVolo.setScadenzaPrenotazione(volo.getScadenzaPrenotazione());
        nuovoVolo.setDataDestinazione(volo.getDataDestinazione());
        nuovoVolo.setNumPosti(volo.getNumPosti());
        nuovoVolo.setPrezzoBiglietto(volo.getPrezzoBiglietto());
        nuovoVolo.setNumBigliettiDisponibili(nuovoVolo.getNumPosti());
        nuovoVolo.setIncasso(0.0);

        repositoryVolo.save(nuovoVolo);
        return nuovoVolo;
    }


    public Volo modificaPrezzoBigliettoVolo(Volo v,String username){
        Volo volo=repositoryVolo.ottieniDaId(v.getId());
        if(volo!=null){
            if(!volo.getCompagniaAerea().getUsername().equals(username)){
                throw new OperazioneNonAutorizzataException("Non puoi modificare i voli delle altre compagnie");
            }
            volo.setPrezzoBiglietto(v.getPrezzoBiglietto());
            repositoryVolo.save(volo);
            return volo;
        }else{
            throw new VoloNonEsistenteException("Il volo che si vuole modificare non esiste");
        }
    }



    @Transactional(rollbackFor={BigliettiFinitiException.class, VoloNonEsistenteException.class, ScadenzaPrenotazioneException.class, PrezzoCambiatoException.class})
    public List<Volo> prenota(List<Volo> voli,String username){
        Utente utente=repositoryUtente.findByUsername(username);
        List<Volo> result=new ArrayList<>();
        for(Volo v:voli){
            result.add(prenotaVolo(v,utente));
        }
        return result;
    }

    @Transactional(propagation = Propagation.NESTED)
    public Volo prenotaVolo(Volo v,Utente utente){
        Optional<Volo> op=repositoryVolo.findById(v.getId());
        if(op.isEmpty())
            throw new VoloNonEsistenteException("Il volo con id "+v.getId()+" non è presente nel database");
        Volo curr=op.get();
        if(curr.getScadenzaPrenotazione().before(Calendar.getInstance()))
            throw new ScadenzaPrenotazioneException("Il tempo per la prenotazione del volo con id "+v.getId()+" è scaduto");
        if(curr.getNumBigliettiDisponibili() == 0)
            throw new BigliettiFinitiException("Biglietti finiti per il volo "+curr.getId());
        if(!curr.getPrezzoBiglietto().equals(v.getPrezzoBiglietto()))
            throw new PrezzoCambiatoException("Il prezzo di vendita del biglietto del volo con id"+v.getId()+" è cambiato");
        Biglietto nuovoBiglietto = new Biglietto();
        curr.setNumBigliettiDisponibili(curr.getNumBigliettiDisponibili()-1);
        curr.setIncasso(curr.getIncasso()+curr.getPrezzoBiglietto());
        nuovoBiglietto.setUtente(utente);
        nuovoBiglietto.setVolo(curr);
        nuovoBiglietto.setDettagli("Numero posto : "+(curr.getNumPosti()-curr.getNumBigliettiDisponibili()));
        nuovoBiglietto.setDataAcquisto(Calendar.getInstance());
        utente.getBiglietti().add(nuovoBiglietto);
        repositoryBiglietto.save(nuovoBiglietto);
        repositoryUtente.save(utente);
        repositoryVolo.save(curr);
        return curr;
    }

    private List<List<Volo>> listaVoliConScalo(List<Object[]> content) {
        List<List<Volo>> voli = new ArrayList<>();
        for(Object[] o:content){
            List<Volo> curr=new ArrayList<>();
            for(Object id:o){
                Long idVolo=(Long)id;
                curr.add(repositoryVolo.ottieniDaId(idVolo));
            }
            voli.add(curr);
        }
        return voli;
    }

    private List<List<Volo>> listaVoliDiretti(List<Volo> l){
        List<List<Volo>> voli = new ArrayList<>();
        for (Volo v : l) {
            ArrayList<Volo> curr=new ArrayList<>();
            curr.add(v);
            voli.add(curr);
        }
        return voli;
    }

}
