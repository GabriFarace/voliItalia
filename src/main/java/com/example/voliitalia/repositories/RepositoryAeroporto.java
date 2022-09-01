package com.example.voliitalia.repositories;

import com.example.voliitalia.entità.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryAeroporto extends JpaRepository<Aeroporto,Long> {
    Aeroporto findByNome(String nome);
}
