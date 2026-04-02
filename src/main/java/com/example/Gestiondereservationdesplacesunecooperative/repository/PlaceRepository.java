package com.example.Gestiondereservationdesplacesunecooperative.repository;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Place;
import com.example.Gestiondereservationdesplacesunecooperative.entity.PlaceId;
import com.example.Gestiondereservationdesplacesunecooperative.entity.Voiture;
import com.example.Gestiondereservationdesplacesunecooperative.enums.OccupationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, PlaceId> {

    List<Place> findByVoitureAndOccupation(Voiture voiture, OccupationStatus occupation);

    int countByVoitureAndOccupation(Voiture voiture, OccupationStatus occupation);
}