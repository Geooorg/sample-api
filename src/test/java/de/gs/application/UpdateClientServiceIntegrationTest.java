package de.gs.application;

import de.gs.api.exposed.dto.UpdateClientDto;
import de.gs.domain.Client;
import de.gs.persistence.ClientRepository;
import de.gs.testdata.ClientDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integrationtest"})
class UpdateClientServiceIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FindClientService findClientService;

    private UpdateClientService subject;

    @BeforeEach
    void cleanUp() {
        clientRepository.deleteAll();
        subject = new UpdateClientService(clientRepository);
    }

    @DisplayName("Client can be added")
    @Test
    void canBeAdded() {
        // given
        Client clientTemplate = ClientDataFactory.createClient("newClient");
        UpdateClientDto updateDto = new UpdateClientDto();
        BeanUtils.copyProperties(clientTemplate, updateDto);

        // when
        String clientId = subject.createClient(updateDto);
        final Optional<Client> foundClient = findClientService.findById(clientId);

        // then
        assertTrue(foundClient.isPresent());
        assertEquals(foundClient.get().getFirstName(), updateDto.getFirstName());
        assertEquals(foundClient.get().getLastName(), updateDto.getLastName());
        // ...
        assertEquals(foundClient.get().getEmail(), updateDto.getEmail());
        // ...
    }

    @DisplayName("Client can be updated")
    @Test
    void canBeUpdated() {
        // given
        Client clientBefore = clientRepository.save(ClientDataFactory.createClient("updateTest"));
        UpdateClientDto updateDto = new UpdateClientDto();
        BeanUtils.copyProperties(clientBefore, updateDto);
        updateDto.setEmail("newEmail@sample.com");

        // when
        Client clientAfter = subject.updateClient(updateDto, clientBefore.getId());

        // then
        assertEquals(clientAfter.getId(), clientBefore.getId()); // id must not change
        assertEquals(clientAfter.getAddress(), clientBefore.getAddress()); // address must not change
        assertEquals("newEmail@sample.com", clientAfter.getEmail()); // email needs to be updated
    }

    @DisplayName("Client can be deleted")
    @Test
    void canBeDeleted() {
        // given
        Client client = clientRepository.save(ClientDataFactory.createClient("sampleUser"));

        // when
        subject.deleteClient(client.getId());
        final Optional<Client> foundClient = findClientService.findById(client.getId());

        // then
       assertTrue(foundClient.isEmpty());
    }
}
