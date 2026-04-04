package com.example.Gestiondereservationdesplacesunecooperative.dto;

import lombok.Data;

@Data
public class RecuDTO {
    private String idReserv;
    private String dateReserv;
    private String dateVoyage;
    private String nomClient;
    private String contact;
    private String idVoiture;
    private String typeVoiture;
    private int place;
    private int frais;
    private String payment;
    private int montantAvance;
    private int resteAPayer;
}