package com.example.Gestiondereservationdesplacesunecooperative.repository;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, String> {

    // retrieves the last inserted voiture ordered by ID descending
    Optional<Voiture> findTopByOrderByIdVoitDesc();

}