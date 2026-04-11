package com.example.Gestiondereservationdesplacesunecooperative.service;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Place;
import com.example.Gestiondereservationdesplacesunecooperative.entity.Voiture;
import com.example.Gestiondereservationdesplacesunecooperative.entity.TypeVoiture;
import com.example.Gestiondereservationdesplacesunecooperative.entity.OccupationStatus;
import com.example.Gestiondereservationdesplacesunecooperative.exception.AppException;
import com.example.Gestiondereservationdesplacesunecooperative.repository.PlaceRepository;
import com.example.Gestiondereservationdesplacesunecooperative.repository.VoitureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoitureService {

    private final VoitureRepository voitureRepository;
    private final PlaceRepository placeRepository;

    // generates the next ID in format VOIN001, VOIN002, ...
    private String generateId() {
        return voitureRepository.findTopByOrderByIdVoitDesc()
                .map(v -> {
                    int lastNumber = Integer.parseInt(v.getIdVoit().replace("VOIN", ""));
                    return String.format("VOIN%03d", lastNumber + 1);
                })
                .orElse("VOIN001");
    }

    // validates that the type is not null
    private void validerType(TypeVoiture type) {
        if (type == null) {
            log.warn("Attempt to insert a null type");
            throw new AppException("Le type de voiture est obligatoire (choix : simple, premium ou VIP).");
        }
    }

    // get all voitures
    public List<Voiture> getAllVoitures() {
        List<Voiture> voitures = voitureRepository.findAll();
        log.info("Retrieved {} voitures successfully", voitures.size());
        return voitures;
    }

    // get voiture by id
    public Voiture getVoitureById(String idVoit) {
        return voitureRepository.findById(idVoit)
                .orElseThrow(() -> {
                    log.error("Voiture {} not found", idVoit);
                    return new AppException("Désolé, la voiture avec l'identifiant " + idVoit + " n'existe pas.");
                });
    }

    // create new voiture and automatically insert all seats as LIBRE
    public Voiture createVoiture(Voiture voiture) {
        validerType(voiture.getType());
        voiture.setIdVoit(generateId());
        Voiture savedVoiture = voitureRepository.save(voiture);

        for (int i = 1; i <= savedVoiture.getNbrPlace(); i++) {
            Place place = new Place();
            place.setVoiture(savedVoiture);
            place.setPlace(i);
            place.setOccupation(OccupationStatus.LIBRE);
            placeRepository.save(place);
        }

        log.info("Voiture {} created with {} seats", savedVoiture.getIdVoit(), savedVoiture.getNbrPlace());
        return savedVoiture;
    }

    // update voiture - nbrPlace cannot be modified
    public Voiture updateVoiture(String idVoit, Voiture voitureDetails) {
        Voiture voiture = voitureRepository.findById(idVoit)
                .orElseThrow(() -> {
                    log.error("Voiture {} not found for update", idVoit);
                    return new AppException("Impossible de mettre à jour : la voiture " + idVoit + " est inexistante.");
                });

        validerType(voitureDetails.getType());

        voiture.setDesign(voitureDetails.getDesign());
        voiture.setType(voitureDetails.getType());
        // nbrPlace is intentionally not updated here
        voiture.setFrais(voitureDetails.getFrais());

        Voiture updatedVoiture = voitureRepository.save(voiture);
        log.info("Voiture {} updated successfully", idVoit);
        return updatedVoiture;
    }

    // delete voiture and all its places via cascade
    public void deleteVoiture(String idVoit) {
        if (!voitureRepository.existsById(idVoit)) {
            log.error("Voiture {} not found for deletion", idVoit);
            throw new AppException("Erreur de suppression : la voiture " + idVoit + " n'a pas été trouvée.");
        }
        voitureRepository.deleteById(idVoit);
        log.info("Voiture {} and all its places deleted successfully", idVoit);
    }
}