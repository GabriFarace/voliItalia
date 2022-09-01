package com.example.voliitalia.servizi;

import com.example.voliitalia.entità.Aeroporto;
import com.example.voliitalia.repositories.RepositoryAeroporto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ServizioAeroporto {
    @Autowired
    private RepositoryAeroporto repositoryAeroporto;

    public List<Aeroporto> getAeroporti(){
        return repositoryAeroporto.findAll();
    }
}
