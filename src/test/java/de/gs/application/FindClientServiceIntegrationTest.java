package de.gs.application;

import de.gs.domain.Client;
import de.gs.persistence.ClientRepository;
import de.gs.testdata.ClientDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
}
