package com.example.voliitalia.entità;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class Aeroporto {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String citta;

    @Column(unique = true,nullable = false)
    private String nome;

    @Column(unique = true)
    private String iata;
}
