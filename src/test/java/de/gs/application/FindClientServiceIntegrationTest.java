package de.gs.application;

import de.gs.api.exposed.dto.FindClientDto;
import de.gs.domain.Client;
import de.gs.persistence.ClientRepository;
import de.gs.testdata.ClientDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integrationtest"})
class FindClientServiceIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    private FindClientService subject;

    @BeforeEach
    void cleanUp() {
        clientRepository.deleteAll();
        subject = new FindClientService(clientRepository);
    }

    @DisplayName("Client can be found by ID")
    @Test
    void canFindById() {
        // given
        Client client = clientRepository.save(ClientDataFactory.createClient("IdFinder"));

        // when
        final Optional<Client> foundClient = subject.findById(client.getId());

        // then
        assertTrue(foundClient.isPresent());
    }

    @DisplayName("Search clients by unique properties like email or Id")
    @Test
    void canFindClientsByEmailOrId() {
        // given
        Client client1 = clientRepository.save(ClientDataFactory.createClient("numberOne"));
        Client client2 = clientRepository.save(ClientDataFactory.createClient("numberTwo"));
        final String emailClient1 = client1.getEmail();
        final String idClient2 = client2.getId();

        FindClientDto findByMailDto = FindClientDto.builder().email(emailClient1).build();
        FindClientDto findByIdDto = FindClientDto.builder().id(idClient2).build();

        // when
        final Page<Client> foundClientsByMail = subject.findClientsBySpecification(findByMailDto, 0, 5);
        final Page<Client> foundClientsById = subject.findClientsBySpecification(findByIdDto, 0, 5);


        // then assert for client1:
        assertEquals(foundClientsByMail.getTotalElements(), 1);
        assertEquals(foundClientsByMail.getContent().get(0).getId(), client1.getId());
        assertEquals(foundClientsByMail.getContent().get(0).getEmail(), emailClient1);

        // then assert for client2:
        assertEquals(foundClientsById.getTotalElements(), 1);
        assertEquals(foundClientsById.getContent().get(0).getId(), idClient2);
        assertEquals(foundClientsById.getContent().get(0).getEmail(), client2.getEmail());
    }

    @DisplayName("Search clients using 'contains ignorecase' search terms")
    @Test
    void canFindClientsByPropertiesContainingSearchTerm() {
        // given
        Client client1 = clientRepository.save(ClientDataFactory.createClient("numberOne"));
        Client client2 = clientRepository.save(ClientDataFactory.createClient("numberTwo"));
        Client client3 = clientRepository.save(ClientDataFactory.createClient("numberThree"));

        FindClientDto findByFirstName = FindClientDto.builder().firstName("number").build();

        // when
        final Page<Client> foundClients = subject.findClientsBySpecification(findByFirstName, 0, 5);


        // then
        assertEquals(foundClients.getTotalElements(), 3);
    }

    @DisplayName("Searching clients using 'contains ignorecase' search terms combined with unique fields like id" +
            " will result in a unique search result")
    @Test
    void canFindClientsByPropertiesContainingSearchTermCombinedWithUniqueFields() {
        // given
        Client client1 = clientRepository.save(ClientDataFactory.createClient("numberOne"));
        Client client2 = clientRepository.save(ClientDataFactory.createClient("numberTwo"));
        Client client3 = clientRepository.save(ClientDataFactory.createClient("numberThree"));

        final String client2Id = client2.getId();
        FindClientDto findByFirstName = FindClientDto.builder()
                .id(client2Id)
                .firstName("number").build();

        // when
        final Page<Client> foundClients = subject.findClientsBySpecification(findByFirstName, 0, 5);


        // then
        assertEquals(foundClients.getTotalElements(), 1);
        assertEquals(foundClients.getContent().get(0).getId(), client2Id);
    }
}
