package com.example.Gestiondereservationdesplacesunecooperative.service;

import com.example.Gestiondereservationdesplacesunecooperative.dto.DashboardDTO;
import com.example.Gestiondereservationdesplacesunecooperative.dto.RecuDTO;
import com.example.Gestiondereservationdesplacesunecooperative.dto.VoyageurDTO;
import com.example.Gestiondereservationdesplacesunecooperative.entity.*;
import com.example.Gestiondereservationdesplacesunecooperative.entity.OccupationStatus;
import com.example.Gestiondereservationdesplacesunecooperative.entity.PaymentType;
import com.example.Gestiondereservationdesplacesunecooperative.exception.AppException;
import com.example.Gestiondereservationdesplacesunecooperative.repository.ClientRepository;
import com.example.Gestiondereservationdesplacesunecooperative.repository.PlaceRepository;
import com.example.Gestiondereservationdesplacesunecooperative.repository.ReservationRepository;
import com.example.Gestiondereservationdesplacesunecooperative.repository.VoitureRepository;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final VoitureRepository voitureRepository;
    private final ClientRepository clientRepository;
    private final PlaceRepository placeRepository;

    /**
     * Genere automatiquement le prochain ID de reservation
     * au format RES001, RES002, RES003, ...
     */
    private String generateId() {
        return reservationRepository.findTopByOrderByIdReservDesc()
                .map(r -> {
                    int lastNumber = Integer.parseInt(r.getIdReserv().replace("RES", ""));
                    return String.format("RES%03d", lastNumber + 1);
                })
                .orElse("RES001");
    }

    /**
     * Recupere la voiture depuis la base de donnees.
     * Lance une exception si la voiture n'existe pas.
     */
    private Voiture findVoiture(String idVoit) {
        return voitureRepository.findById(idVoit)
                .orElseThrow(() -> new AppException("Voiture " + idVoit + " introuvable."));
    }

    /**
     * Recupere le client depuis la base de donnees.
     * Lance une exception si le client n'existe pas.
     */
    private Client findClient(int idCli) {
        return clientRepository.findById(idCli)
                .orElseThrow(() -> new AppException("Client " + idCli + " introuvable."));
    }

    /**
     * Valide le type de paiement.
     * Lance une exception si le paiement est nul.
     */
    private void validerPaiement(PaymentType payment) {
        if (payment == null) {
            throw new AppException("Le type de paiement est obligatoire (SANS_AVANCE, AVEC_AVANCE, TOUT_PAYE).");
        }
    }

    /**
     * Retourne la liste de toutes les reservations enregistrees.
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        log.info("Recuperation de {} reservations avec succes", reservations.size());
        return reservations;
    }

    /**
     * Retourne une reservation par son identifiant.
     * Lance une exception si la reservation n'existe pas.
     */
    public Reservation getReservationById(String idReserv) {
        return reservationRepository.findById(idReserv)
                .orElseThrow(() -> {
                    log.error("Reservation {} introuvable", idReserv);
                    return new AppException("Reservation " + idReserv + " introuvable.");
                });
    }

    /**
     * Retourne toutes les reservations d'un client donne.
     */
    public List<Reservation> getReservationsByClient(int idCli) {
        Client client = findClient(idCli);
        return reservationRepository.findByClient(client);
    }

    /**
     * Retourne toutes les reservations d'une voiture donnee.
     */
    public List<Reservation> getReservationsByVoiture(String idVoit) {
        Voiture voiture = findVoiture(idVoit);
        return reservationRepository.findByVoiture(voiture);
    }

    /**
     * Cree une nouvelle reservation en verifiant :
     * - que la voiture existe
     * - que le client existe
     * - que la place demandee est disponible (statut LIBRE)
     * - que le type de paiement est valide
     * Apres la reservation, la place est automatiquement marquee comme OCCUPE.
     */
    public Reservation createReservation(String idVoit, int idCli, Reservation reservation) {
        Voiture voiture = findVoiture(idVoit);
        Client client = findClient(idCli);
        validerPaiement(reservation.getPayment());

        // verifie que la place n'est pas deja reservee
        if (reservationRepository.existsByVoitureAndPlace(voiture, reservation.getPlace())) {
            throw new AppException("La place " + reservation.getPlace() + " est deja reservee pour cette voiture.");
        }

        // marque la place comme OCCUPE dans la table places
        PlaceId placeId = new PlaceId();
        placeId.setVoiture(idVoit);
        placeId.setPlace(reservation.getPlace());

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new AppException("La place " + reservation.getPlace() + " n'existe pas pour cette voiture."));
        place.setOccupation(OccupationStatus.OCCUPE);
        placeRepository.save(place);

        // configure et sauvegarde la reservation
        reservation.setIdReserv(generateId());
        reservation.setVoiture(voiture);
        reservation.setClient(client);
        reservation.setDateReserv(LocalDateTime.now());

        Reservation saved = reservationRepository.save(reservation);
        log.info("Reservation {} creee avec succes pour le client {} dans la voiture {}", saved.getIdReserv(), idCli, idVoit);
        return saved;
    }

    /**
     * Met a jour le type de paiement d'une reservation existante.
     * Seul le paiement et le montant avance peuvent etre modifies.
     */
    /**
     * Met a jour une reservation existante.
     * Permet de modifier le paiement, le montant et la PLACE.
     */
    public Reservation updateReservation(String idReserv, Reservation reservationDetails) {
        // 1. Chercher la reservation existante
        Reservation reservation = reservationRepository.findById(idReserv)
                .orElseThrow(() -> {
                    log.error("Reservation {} introuvable pour mise a jour", idReserv);
                    return new AppException("Impossible de mettre a jour : reservation " + idReserv + " introuvable.");
                });

        validerPaiement(reservationDetails.getPayment());

        // 2. Gérer le changement de PLACE si nécessaire
        int anciennePlaceNum = reservation.getPlace();
        int nouvellePlaceNum = reservationDetails.getPlace();
        String idVoit = reservation.getVoiture().getIdVoit();

        if (anciennePlaceNum != nouvellePlaceNum) {
            log.info("Changement de place detecte pour RES {}: {} -> {}", idReserv, anciennePlaceNum, nouvellePlaceNum);

            // A. Verifier si la NOUVELLE place est disponible
            PlaceId newPlaceId = new PlaceId();
            newPlaceId.setVoiture(idVoit);
            newPlaceId.setPlace(nouvellePlaceNum);

            Place nouvellePlace = placeRepository.findById(newPlaceId)
                    .orElseThrow(() -> new AppException("La place " + nouvellePlaceNum + " n'existe pas pour cette voiture."));

            if (nouvellePlace.getOccupation() == OccupationStatus.OCCUPE) {
                throw new AppException("La place " + nouvellePlaceNum + " est deja occupee par un autre client.");
            }

            // B. Liberer l'ANCIENNE place
            PlaceId oldPlaceId = new PlaceId();
            oldPlaceId.setVoiture(idVoit);
            oldPlaceId.setPlace(anciennePlaceNum);

            Place anciennePlace = placeRepository.findById(oldPlaceId)
                    .orElseThrow(() -> new AppException("Erreur critique : ancienne place introuvable."));

            anciennePlace.setOccupation(OccupationStatus.LIBRE);
            placeRepository.save(anciennePlace);

            // C. Occuper la NOUVELLE place
            nouvellePlace.setOccupation(OccupationStatus.OCCUPE);
            placeRepository.save(nouvellePlace);

            // D. Mettre a jour le numero de place dans la reservation
            reservation.setPlace(nouvellePlaceNum);
        }

        // 3. Mettre a jour les autres champs (paiement, montant, date voyage)
        reservation.setPayment(reservationDetails.getPayment());
        reservation.setMontantAvance(reservationDetails.getMontantAvance());

        // Optionnel : permettre aussi de modifier la date du voyage
        if (reservationDetails.getDateVoyage() != null) {
            reservation.setDateVoyage(reservationDetails.getDateVoyage());
        }

        Reservation updated = reservationRepository.save(reservation);
        log.info("Reservation {} mise a jour avec succes (Nouvelle place: {})", idReserv, updated.getPlace());
        return updated;
    }

    /**
     * Supprime une reservation et remet automatiquement
     * la place correspondante en statut LIBRE.
     */
    public void deleteReservation(String idReserv) {
        Reservation reservation = reservationRepository.findById(idReserv)
                .orElseThrow(() -> {
                    log.error("Reservation {} introuvable pour suppression", idReserv);
                    return new AppException("Erreur de suppression : reservation " + idReserv + " introuvable.");
                });

        // remet la place en statut LIBRE apres annulation
        PlaceId placeId = new PlaceId();
        placeId.setVoiture(reservation.getVoiture().getIdVoit());
        placeId.setPlace(reservation.getPlace());

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new AppException("Place introuvable pour liberation."));
        place.setOccupation(OccupationStatus.LIBRE);
        placeRepository.save(place);

        reservationRepository.deleteById(idReserv);
        log.info("Reservation {} supprimee et place {} remise en LIBRE", idReserv, reservation.getPlace());
    }

    public List<VoyageurDTO> getSuiviVoyageurs(String idVoit) {
        Voiture voiture = findVoiture(idVoit);
        List<Reservation> reservations = reservationRepository.findByVoiture(voiture);

        return reservations.stream().map(r -> {
            VoyageurDTO dto = new VoyageurDTO();
            dto.setIdReserv(r.getIdReserv());
            dto.setPlace(r.getPlace());
            dto.setNomClient(r.getClient().getNom());
            dto.setNumTel(r.getClient().getNumTel());
            dto.setStatutPaiement(r.getPayment());
            dto.setFrais(voiture.getFrais());
            dto.setMontantAvance(r.getMontantAvance());
            dto.setResteAPayer(voiture.getFrais() - r.getMontantAvance());


            // On récupère la date de la réservation et on la transforme en texte
            if (r.getDateVoyage() != null) {
                dto.setDateVoyage(r.getDateVoyage().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            // -------------------------------------

            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }

    public DashboardDTO getDashboard(String idVoit) {
        Voiture voiture = findVoiture(idVoit);
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setTotalPassagers(reservationRepository.countByVoiture(voiture));
        dashboard.setTotalToutPaye(reservationRepository.countByVoitureAndPayment(voiture, PaymentType.TOUT_PAYE));
        dashboard.setTotalResteAPayer(dashboard.getTotalPassagers() - dashboard.getTotalToutPaye());
        return dashboard;
    }

    public RecuDTO getRecu(String idReserv) {
        Reservation reservation = reservationRepository.findById(idReserv)
                .orElseThrow(() -> new AppException("Reservation " + idReserv + " introuvable."));

        RecuDTO recu = new RecuDTO();
        recu.setIdReserv(reservation.getIdReserv());
        recu.setDateReserv(reservation.getDateReserv().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")));
        recu.setDateVoyage(reservation.getDateVoyage().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        recu.setNomClient(reservation.getClient().getNom());
        recu.setContact(reservation.getClient().getNumTel());
        recu.setIdVoiture(reservation.getVoiture().getIdVoit());
        recu.setTypeVoiture(reservation.getVoiture().getType().toString());
        recu.setPlace(reservation.getPlace());
        recu.setFrais(reservation.getVoiture().getFrais());
        recu.setPayment(reservation.getPayment().toString().replace("_", " "));
        recu.setMontantAvance(reservation.getMontantAvance());
        recu.setResteAPayer(reservation.getVoiture().getFrais() - reservation.getMontantAvance());

        return recu;
    }


    public int getTotalRecetteAccumulee() {
        Integer total = reservationRepository.sumAllRecettes();
        return (total != null) ? total : 0;
    }

}