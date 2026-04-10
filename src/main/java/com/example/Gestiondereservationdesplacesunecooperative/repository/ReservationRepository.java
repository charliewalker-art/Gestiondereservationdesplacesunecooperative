package com.example.Gestiondereservationdesplacesunecooperative.repository;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Reservation;
import com.example.Gestiondereservationdesplacesunecooperative.entity.Voiture;
import com.example.Gestiondereservationdesplacesunecooperative.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.example.Gestiondereservationdesplacesunecooperative.enums.PaymentType;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    // retrieves the last inserted reservation ordered by ID descending
    Optional<Reservation> findTopByOrderByIdReservDesc();

    // retrieves all reservations by voiture
    List<Reservation> findByVoiture(Voiture voiture);

    // retrieves all reservations by client
    List<Reservation> findByClient(Client client);

    // checks if a place is already reserved for a given voiture
    boolean existsByVoitureAndPlace(Voiture voiture, int place);

    int countByVoiture(Voiture voiture);
    int countByVoitureAndPayment(Voiture voiture, PaymentType payment);

    @Query("SELECT SUM(r.montantAvance) FROM Reservation r")
    Integer sumAllRecettes();
}