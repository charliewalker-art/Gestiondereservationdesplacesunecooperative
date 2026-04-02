package com.example.Gestiondereservationdesplacesunecooperative.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing a client in the system.
 */
@Entity
@Table(name = "client")
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cli")
    private int idCli;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "num_tel", nullable = false)
    private String numTel;
}