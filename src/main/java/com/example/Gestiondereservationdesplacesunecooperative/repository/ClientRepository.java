package com.example.Gestiondereservationdesplacesunecooperative.repository;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    // checks if a client exists by phone number
    boolean existsByNumTel(String numTel);
}