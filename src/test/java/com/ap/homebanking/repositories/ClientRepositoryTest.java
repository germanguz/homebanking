package com.ap.homebanking.repositories;

import com.ap.homebanking.models.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    // verifica que exista un cliente con el email proporcionado
    @Test
    public void findByEmail() {
        Client client = clientRepository.findByEmail("admin@mindhub.com");
        assertThat(client, notNullValue());
    }

    // verifica que existan clientes
    @Test
    public void notEmptyList() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    // verifica que en los clientes exista un email
    @Test
    public void mailNotNull() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            assertThat(client.getEmail(), notNullValue());
        }
    }

}

