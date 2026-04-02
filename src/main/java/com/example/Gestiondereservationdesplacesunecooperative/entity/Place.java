package com.example.Gestiondereservationdesplacesunecooperative.entity;

import com.example.Gestiondereservationdesplacesunecooperative.enums.OccupationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "place")
@IdClass(PlaceId.class)
@Data
public class Place {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_voit", nullable = false)
    @JsonIgnore  // prevents infinite loop when serializing to JSON
    private Voiture voiture;

    @Id
    @Column(name = "place", nullable = false)
    private int place;

    @Enumerated(EnumType.STRING)
    @Column(name = "occupation", nullable = false)
    private OccupationStatus occupation;
}