package com.ap.homebanking.services;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    // getCLients fue dividido en dos: getAllClients y getClientsDTO a modo de prueba para separar tareas
    List<Client> getAllClients();

    List<ClientDTO> getClientsDTO(List<Client> clients);

    Client getClientByEmail(String email);

    void saveClient(Client client);

    ClientDTO getClientDTOById(Long id);


}
