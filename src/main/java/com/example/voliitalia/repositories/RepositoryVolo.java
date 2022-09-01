package com.example.voliitalia.repositories;

import com.example.voliitalia.entità.Volo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

@Repository
public interface RepositoryVolo extends JpaRepository<Volo,Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Volo> findById(Long id);

    @Query("select v from Volo v where v.id=:id")
    Volo ottieniDaId(Long id);


    @Query("select v from Volo v where v.compagniaAerea.username=:usernameCompagnia and v.scadenzaPrenotazione>current_timestamp order by v.dataPartenza asc")
    Page<Volo> findVoliNonPartitiCompagnia(String usernameCompagnia,Pageable pageable);


    @Query("select v from Volo v where v.compagniaAerea.username=:usernameCompagnia and v.scadenzaPrenotazione<current_timestamp order by v.dataPartenza desc ")
    Page<Volo> findVoliPartitiCompagnia(String usernameCompagnia,Pageable pageable);




    @Query("select v from Volo v where " +
            "(:cittaPartenza = '' or v.aeroportoPartenza.citta=:cittaPartenza )              and "+
            "(:cittaDestinazione = '' or v.aeroportoDestinazione.citta=:cittaDestinazione )  and "+
            "(v.dataPartenza>:dataPartenza) and (v.dataPartenza < :dataGiornoDopo)           and "+
            "(v.scadenzaPrenotazione>current_timestamp)                                      and "+
            "(:nomeCompagnia = '' or v.compagniaAerea.nome=:nomeCompagnia )                  and "+
            "(:prezzo = 0.0 or v.prezzoBiglietto<=:prezzo) order by v.dataPartenza")
    Page<Volo> ricercaAvanzataVoliDiretti(String cittaPartenza, String cittaDestinazione, Calendar dataPartenza,
                               Calendar dataGiornoDopo,String nomeCompagnia, Double prezzo, Pageable pageable );







    @Query("select v1.id,v2.id from Volo v1,Volo v2 where " +
            "(v1.aeroportoPartenza.citta=:cittaPartenza and v1.aeroportoDestinazione.citta=v2.aeroportoPartenza.citta  and" +
            " v2.aeroportoDestinazione.citta=:cittaDestinazione ) and (v1.dataDestinazione<v2.dataPartenza)            and "+
            "(v1.dataPartenza>:dataPartenza) and (v1.dataPartenza<:dataGiornoDopo )                                    and "+
            "(v1.scadenzaPrenotazione>current_timestamp)                                                               and "+
            "(:nomeCompagnia='' or (v1.compagniaAerea.nome=:nomeCompagnia and v2.compagniaAerea.nome=:nomeCompagnia))  and "+
            "(:prezzo = 0.0 or (v1.prezzoBiglietto+v2.prezzoBiglietto<=:prezzo)) order by v1.dataPartenza")
    Page<Object[]> ricercaAvanzataVoliConSingoloScalo(String cittaPartenza, String cittaDestinazione, Calendar dataPartenza,
                                                      Calendar dataGiornoDopo,String nomeCompagnia, Double prezzo, Pageable pageable);





    @Query("select v1.id,v2.id,v3.id from Volo v1,Volo v2,Volo v3 where " +
            "(v1.aeroportoPartenza.citta=:cittaPartenza and v1.aeroportoDestinazione.citta=v2.aeroportoPartenza.citta" +
            " and v2.aeroportoDestinazione.citta=v3.aeroportoPartenza.citta and v3.aeroportoDestinazione.citta=:cittaDestinazione) and"+
            "(v1.dataPartenza>:dataPartenza) and (v1.dataPartenza<:dataGiornoDopo)               and" +
            "(v1.scadenzaPrenotazione>current_timestamp)                                         and "+
            "(v1.dataDestinazione<v2.dataPartenza and v2.dataDestinazione<v3.dataPartenza)       and "+
            "(:nomeCompagnia='' or  (v1.compagniaAerea.nome=:nomeCompagnia                       and" +
            " v2.compagniaAerea.nome=:nomeCompagnia and v3.compagniaAerea.nome=:nomeCompagnia))  and "+
            "(:prezzo = 0.0 or (v1.prezzoBiglietto+v2.prezzoBiglietto+v3.prezzoBiglietto<=:prezzo))order by v1.dataPartenza")
    Page<Object[]> ricercaAvanzataVoliConDoppioScalo(String cittaPartenza, String cittaDestinazione, Calendar dataPartenza,
                                                     Calendar dataGiornoDopo,String nomeCompagnia, Double prezzo, Pageable pageable);


}
