package com.example.voliitalia.entità;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false , updatable= false)
    @JsonIgnore
    private Long id;

    private String username;

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nome;

    private String cognome;

    @JsonIgnore
    @OneToMany( mappedBy="utente")
    private List<Biglietto> biglietti;

}
