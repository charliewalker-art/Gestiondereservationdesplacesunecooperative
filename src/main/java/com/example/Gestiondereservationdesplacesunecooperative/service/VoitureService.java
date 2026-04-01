package com.example.Gestiondereservationdesplacesunecooperative.service;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Voiture;
import com.example.Gestiondereservationdesplacesunecooperative.entity.TypeVoiture;
import com.example.Gestiondereservationdesplacesunecooperative.exception.AppException;
import com.example.Gestiondereservationdesplacesunecooperative.repository.VoitureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Permet d'utiliser log.info pour les messages de succès
public class VoitureService {

    private final VoitureRepository voitureRepository;

    // Génère le prochain ID au format VOIN001, VOIN002, ...
    private String generateId() {
        return voitureRepository.findTopByOrderByIdVoitDesc()
                .map(v -> {
                    int lastNumber = Integer.parseInt(v.getIdVoit().replace("VOIN", ""));
                    return String.format("VOIN%03d", lastNumber + 1);
                })
                .orElse("VOIN001");
    }

    // Récupérer toutes les voitures
    public List<Voiture> getAllVoitures() {
        List<Voiture> voitures = voitureRepository.findAll();
        log.info("Récupération de {} voitures avec succès", voitures.size());
        return voitures;
    }

    // Récupérer une voiture par ID
    public Voiture getVoitureById(String idVoit) {
        return voitureRepository.findById(idVoit)
                .orElseThrow(() -> {
                    log.error("Échec de la récupération : Voiture {} introuvable", idVoit);
                    return new AppException("Désolé, la voiture avec l'identifiant " + idVoit + " n'existe pas.");
                });
    }

    // Créer une nouvelle voiture
    public Voiture createVoiture(Voiture voiture) {
        validerType(voiture.getType());

        voiture.setIdVoit(generateId());
        Voiture savedVoiture = voitureRepository.save(voiture);

        log.info("Succès : La voiture {} a été créée avec succès", savedVoiture.getIdVoit());
        return savedVoiture;
    }

    // Mettre à jour une voiture
    public Voiture updateVoiture(String idVoit, Voiture voitureDetails) {
        Voiture voiture = voitureRepository.findById(idVoit)
                .orElseThrow(() -> {
                    log.error("Échec de la mise à jour : Voiture {} introuvable", idVoit);
                    return new AppException("Impossible de mettre à jour : la voiture " + idVoit + " est inexistante.");
                });

        validerType(voitureDetails.getType());

        voiture.setDesign(voitureDetails.getDesign());
        voiture.setType(voitureDetails.getType());
        voiture.setNbrPlace(voitureDetails.getNbrPlace());
        voiture.setFrais(voitureDetails.getFrais());

        Voiture updatedVoiture = voitureRepository.save(voiture);
        log.info("Succès : La voiture {} a été mise à jour", idVoit);
        return updatedVoiture;
    }

    // Supprimer une voiture
    public void deleteVoiture(String idVoit) {
        if (!voitureRepository.existsById(idVoit)) {
            log.error("Échec de la suppression : Voiture {} introuvable", idVoit);
            throw new AppException("Erreur de suppression : la voiture " + idVoit + " n'a pas été trouvée.");
        }
        voitureRepository.deleteById(idVoit);
        log.info("Succès : La voiture {} a été supprimée définitivement", idVoit);
    }

    // Validation du type
    private void validerType(TypeVoiture type) {
        if (type == null) {
            log.warn("Tentative d'insertion d'un type nul");
            throw new AppException("Le type de voiture est obligatoire (choix : simple, premium ou VIP).");
        }
    }
}