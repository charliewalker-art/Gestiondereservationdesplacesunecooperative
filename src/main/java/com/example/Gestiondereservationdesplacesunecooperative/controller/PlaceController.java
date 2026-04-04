package com.example.Gestiondereservationdesplacesunecooperative.controller;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Place;
import com.example.Gestiondereservationdesplacesunecooperative.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PlaceController {

    private final PlaceService placeService;

    // returns the full list of free places for a given voiture
    @GetMapping("/{idVoit}/libre")
    public List<Place> getPlacesLibres(@PathVariable String idVoit) {
        return placeService.getPlacesLibres(idVoit);
    }

    // returns only the count of free places for a given voiture
    @GetMapping("/{idVoit}/libre/count")
    public int countPlacesLibres(@PathVariable String idVoit) {
        return placeService.countPlacesLibres(idVoit);
    }

    @GetMapping("/{idVoit}/toutes")
    public List<Place> getAllPlaces(@PathVariable String idVoit) {
        return placeService.getAllPlacesByVoiture(idVoit);
    }
}
