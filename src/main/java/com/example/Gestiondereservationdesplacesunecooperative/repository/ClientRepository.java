package com.example.Gestiondereservationdesplacesunecooperative.repository;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    // checks if a client exists by phone number
    boolean existsByNumTel(String numTel);

    List<Client> findByNomContainingOrNumTel(String nom, String numTel);
}