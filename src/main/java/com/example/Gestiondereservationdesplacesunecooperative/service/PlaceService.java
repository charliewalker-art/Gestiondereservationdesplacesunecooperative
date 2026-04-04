package com.example.Gestiondereservationdesplacesunecooperative.service;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Place;
import com.example.Gestiondereservationdesplacesunecooperative.entity.Voiture;
import com.example.Gestiondereservationdesplacesunecooperative.enums.OccupationStatus;
import com.example.Gestiondereservationdesplacesunecooperative.exception.AppException;
import com.example.Gestiondereservationdesplacesunecooperative.repository.PlaceRepository;
import com.example.Gestiondereservationdesplacesunecooperative.repository.VoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final VoitureRepository voitureRepository;

    // Récupère la voiture ou lève une exception si elle est introuvable.
    private Voiture findVoitureById(String idVoit) {
        return voitureRepository.findById(idVoit)
                .orElseThrow(() -> new AppException("Voiture not found"));
    }

    // rend toutes les places libres pour une voiture donnée
    public List<Place> getPlacesLibres(String idVoit) {
        Voiture voiture = findVoitureById(idVoit);
        return placeRepository.findByVoitureAndOccupation(voiture, OccupationStatus.LIBRE);
    }

    // renvoie le nombre de places libres pour une voiture donnée
    public int countPlacesLibres(String idVoit) {
        Voiture voiture = findVoitureById(idVoit);
        return placeRepository.countByVoitureAndOccupation(voiture, OccupationStatus.LIBRE);
    }

    public List<Place> getAllPlacesByVoiture(String idVoit) {
        Voiture voiture = findVoitureById(idVoit);
        return placeRepository.findByVoiture(voiture);
    }
}