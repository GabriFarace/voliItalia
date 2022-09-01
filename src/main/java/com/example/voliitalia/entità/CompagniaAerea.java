package com.example.voliitalia.entità;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CompagniaAerea {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column( unique = true)
    private String nome;

    private String nazione;

    private String sede;
}
