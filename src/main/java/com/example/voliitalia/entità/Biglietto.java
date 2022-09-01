package com.example.voliitalia.entità;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Calendar;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class Biglietto {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @Column( nullable=false, updatable=false)
    private Long id;

    @Basic
    private String dettagli;

    @ManyToOne
    @JoinColumn( name = "utente")
    private Utente utente;

    @ManyToOne
    @JoinColumn( name= "volo")
    private Volo volo;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataAcquisto;
}
