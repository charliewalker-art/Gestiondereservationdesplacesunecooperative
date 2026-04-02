package com.example.Gestiondereservationdesplacesunecooperative.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class PlaceId implements Serializable {
    private String voiture;
    private int place;
}