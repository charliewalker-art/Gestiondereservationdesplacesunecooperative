package com.example.Gestiondereservationdesplacesunecooperative.controller;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Voiture;
import com.example.Gestiondereservationdesplacesunecooperative.service.VoitureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/voiture")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class VoitureController {

    private final VoitureService voitureService;

    // get all voitures
    @GetMapping
    public List<Voiture> getAllVoitures() {
        return voitureService.getAllVoitures();
    }

    // get voiture by id
    @GetMapping("/{idVoit}")
    public Voiture getVoitureById(@PathVariable String idVoit) {
        return voitureService.getVoitureById(idVoit);
    }

    // create new voiture
    @PostMapping
    public Voiture createVoiture(@RequestBody Voiture voiture) {
        return voitureService.createVoiture(voiture);
    }

    // update voiture
    @PutMapping("/{idVoit}")
    public Voiture updateVoiture(@PathVariable String idVoit, @RequestBody Voiture voitureDetails) {
        return voitureService.updateVoiture(idVoit, voitureDetails);
    }

    // delete voiture
    @DeleteMapping("/{idVoit}")
    public String deleteVoiture(@PathVariable String idVoit) {
        voitureService.deleteVoiture(idVoit);
        return "Voiture deleted successfully";
    }
}