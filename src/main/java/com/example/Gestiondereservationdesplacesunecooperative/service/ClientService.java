package com.example.Gestiondereservationdesplacesunecooperative.service;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Client;
import com.example.Gestiondereservationdesplacesunecooperative.exception.AppException;
import com.example.Gestiondereservationdesplacesunecooperative.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    // get all clients
    public List<Client> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        log.info("Retrieved {} clients successfully", clients.size());
        return clients;
    }

    // get client by id
    public Client getClientById(int idCli) {
        return clientRepository.findById(idCli)
                .orElseThrow(() -> {
                    log.error("Client {} not found", idCli);
                    return new AppException("Client avec l'id " + idCli + " introuvable.");
                });
    }

    // create new client
    public Client createClient(Client client) {
        Client saved = clientRepository.save(client);
        log.info("Client {} created successfully", saved.getIdCli());
        return saved;
    }

    // update client

    public Client updateClient(int idCli, Client clientDetails) {
        Client client = clientRepository.findById(idCli)
                .orElseThrow(() -> {
                    log.error("Client {} not found for update", idCli);
                    return new AppException("Impossible de mettre à jour : client " + idCli + " introuvable.");
                });

        client.setNom(clientDetails.getNom());
        client.setNumTel(clientDetails.getNumTel());

        Client updated = clientRepository.save(client);
        log.info("Client {} updated successfully", idCli);
        return updated;
    }

    // delete client by id
    public void deleteClient(int idCli) {
        if (!clientRepository.existsById(idCli)) {
            log.error("Client {} not found for deletion", idCli);
            throw new AppException("Erreur de suppression : client " + idCli + " introuvable.");
        }
        clientRepository.deleteById(idCli);
        log.info("Client {} deleted successfully", idCli);
    }
}