package com.example.Gestiondereservationdesplacesunecooperative.entity;

import com.example.Gestiondereservationdesplacesunecooperative.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a reservation in the system.
 */
@Entity
@Table(name = "reservation")
@Data
public class Reservation {

    @Id
    @Column(name = "id_reserv", nullable = false, unique = true)
    private String idReserv;

    @ManyToOne
    @JoinColumn(name = "id_voit", nullable = false)
    private Voiture voiture;

    @ManyToOne
    @JoinColumn(name = "id_cli", nullable = false)
    private Client client;

    @Column(name = "place", nullable = false)
    private int place;

    @Column(name = "date_voyage", nullable = false)
    private LocalDate dateVoyage;

    @Column(name = "date_reserv", nullable = false)
    private LocalDateTime dateReserv;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment", nullable = false)
    private PaymentType payment;

    @Column(name = "montant_avance", nullable = false)
    private int montantAvance;
}