package com.example.Gestiondereservationdesplacesunecooperative.dto;

import com.example.Gestiondereservationdesplacesunecooperative.enums.PaymentType;
import lombok.Data;

@Data
public class VoyageurDTO {
    private int place;
    private String nomClient;
    private String numTel;
    private PaymentType statutPaiement;
    private int frais;
    private int montantAvance;
    private int resteAPayer;
}