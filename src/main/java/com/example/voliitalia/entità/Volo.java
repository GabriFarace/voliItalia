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
public class Volo {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn( name = "aeroporto_partenza")
    private Aeroporto aeroportoPartenza;

    @ManyToOne
    @JoinColumn( name = "aeroporto_destinazione")
    private Aeroporto aeroportoDestinazione;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar scadenzaPrenotazione;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataPartenza;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataDestinazione;

    @ManyToOne
    @JoinColumn( name = "compagnia_aerea")
    private CompagniaAerea compagniaAerea;

    private Integer numPosti;

    private Double prezzoBiglietto;

    private Integer numBigliettiDisponibili;

    private Double incasso;



}
