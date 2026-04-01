package com.example.Gestiondereservationdesplacesunecooperative.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.util.Arrays;
import java.util.List;

/**
 * Entity representing a car (voiture) in the system.
 */
@Entity
@Table(name = "voiture")
@Data
public class Voiture {

    @Id
    @Column(name = "id_voit", nullable = false, unique = true)
    private String idVoit;

    @Column(name = "design", nullable = false)
    private String design;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeVoiture type;

    @Column(name = "nbr_place", nullable = false)
    private int nbrPlace;

    @Column(name = "frais", nullable = false)
    private int frais;
}
