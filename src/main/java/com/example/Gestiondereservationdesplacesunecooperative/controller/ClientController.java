package com.example.Gestiondereservationdesplacesunecooperative.controller;

import com.example.Gestiondereservationdesplacesunecooperative.entity.Client;
import com.example.Gestiondereservationdesplacesunecooperative.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // get all clients
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    // get client by id
    @GetMapping("/{idCli}")
    public Client getClientById(@PathVariable int idCli) {
        return clientService.getClientById(idCli);
    }

    // create new client
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    // update client
    @PutMapping("/{idCli}")
    public Client updateClient(@PathVariable int idCli, @RequestBody Client clientDetails) {
        return clientService.updateClient(idCli, clientDetails);
    }

    // delete client
    @DeleteMapping("/{idCli}")
    public String deleteClient(@PathVariable int idCli) {
        clientService.deleteClient(idCli);
        return "Client deleted successfully";
    }
}