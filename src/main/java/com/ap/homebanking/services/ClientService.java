package com.ap.homebanking.services;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getClients();

    //ClientDTO getClientDTOByEmail(String email);
    Client getClientByEmail(String email);

    void saveClient(Client client);

    ClientDTO getClientDTOById(Long id);


}
