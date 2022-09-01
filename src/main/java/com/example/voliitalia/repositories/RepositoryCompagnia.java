package com.example.voliitalia.repositories;

import com.example.voliitalia.entità.CompagniaAerea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryCompagnia extends JpaRepository<CompagniaAerea,Long> {

    CompagniaAerea findByUsername(String username);

    CompagniaAerea findByNome(String nome);
}
