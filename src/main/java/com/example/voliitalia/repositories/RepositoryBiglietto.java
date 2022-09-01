package com.example.voliitalia.repositories;

import com.example.voliitalia.entità.Biglietto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositoryBiglietto extends JpaRepository<Biglietto,Long> {

    @Query("select b from Biglietto b where b.utente.username=:username and b.volo.dataDestinazione>current_timestamp order by b.dataAcquisto desc")
    Page<Biglietto> findByUserUsername(String username, Pageable pageable);


    @Query("select b from Biglietto b where b.utente.username=:username and b.volo.dataDestinazione<current_timestamp order by b.dataAcquisto desc")
    Page<Biglietto> findByUserUsernameScaduti(String username,Pageable pageable);
}
