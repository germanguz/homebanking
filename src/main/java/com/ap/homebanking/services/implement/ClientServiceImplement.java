package com.ap.homebanking.services.implement;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    //El sgte método fue dividido en los 2 métodos que siguen a continuación, linea 26 y 31
//    @Override
//    public List<ClientDTO> getClients() {
//        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
//    }
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> getClientsDTO(List<Client> clients) {
        return clients.stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public ClientDTO getClientDTOById(Long id) {
        return clientRepository.findById(id).map(clientDTO -> new ClientDTO(clientDTO)).orElse(null);
    }
}
