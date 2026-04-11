package com.example.Gestiondereservationdesplacesunecooperative.controller;

import com.example.Gestiondereservationdesplacesunecooperative.dto.DashboardDTO;
import com.example.Gestiondereservationdesplacesunecooperative.dto.RecuDTO;
import com.example.Gestiondereservationdesplacesunecooperative.dto.VoyageurDTO;
import com.example.Gestiondereservationdesplacesunecooperative.entity.Reservation;
import com.example.Gestiondereservationdesplacesunecooperative.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.origin}")
public class ReservationController {

    private final ReservationService reservationService;

    // retourne toutes les reservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // retourne une reservation par son id
    @GetMapping("/{idReserv}")
    public Reservation getReservationById(@PathVariable String idReserv) {
        return reservationService.getReservationById(idReserv);
    }

    // retourne toutes les reservations d'un client
    @GetMapping("/client/{idCli}")
    public List<Reservation> getReservationsByClient(@PathVariable int idCli) {
        return reservationService.getReservationsByClient(idCli);
    }

    // retourne toutes les reservations d'une voiture
    @GetMapping("/voiture/{idVoit}")
    public List<Reservation> getReservationsByVoiture(@PathVariable String idVoit) {
        return reservationService.getReservationsByVoiture(idVoit);
    }

    // cree une nouvelle reservation
    @PostMapping("/{idVoit}/{idCli}")
    public Reservation createReservation(
            @PathVariable String idVoit,
            @PathVariable int idCli,
            @RequestBody Reservation reservation) {
        return reservationService.createReservation(idVoit, idCli, reservation);
    }

    // met a jour le paiement d'une reservation
    @PutMapping("/{idReserv}")
    public Reservation updateReservation(
            @PathVariable String idReserv,
            @RequestBody Reservation reservationDetails) {
        return reservationService.updateReservation(idReserv, reservationDetails);
    }

    // supprime une reservation et libere la place
    @DeleteMapping("/{idReserv}")
    public String deleteReservation(@PathVariable String idReserv) {
        reservationService.deleteReservation(idReserv);
        return "Reservation supprimee et place remise en LIBRE avec succes";
    }

    @GetMapping("/dashboard/{idVoit}")
    public DashboardDTO getDashboard(@PathVariable String idVoit) {
        return reservationService.getDashboard(idVoit);
    }

    @GetMapping("/voyageurs/{idVoit}")
    public List<VoyageurDTO> getSuiviVoyageurs(@PathVariable String idVoit) {
        return reservationService.getSuiviVoyageurs(idVoit);
    }

    @GetMapping("/recu/{idReserv}")
    public RecuDTO getRecu(@PathVariable String idReserv) {
        return reservationService.getRecu(idReserv);
    }

    // retourne la recette totale accumulee par la cooperative
    @GetMapping("/recette-totale")
    public int getTotalRecette() {
        return reservationService.getTotalRecetteAccumulee();
    }
}
