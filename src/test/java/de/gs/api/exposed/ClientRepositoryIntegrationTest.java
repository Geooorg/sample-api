package de.gs.api.exposed;

import de.gs.domain.Client;
import de.gs.persistence.ClientRepository;
import de.gs.testdata.ClientDataFactory;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integrationtest"})
public class ClientRepositoryIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void cleanUp() {
        clientRepository.deleteAll();
    }


    @DisplayName("Basic bean validation test regarding the properties of the Client entity")
    @Test(expected = TransactionSystemException.class)
    public void incompleteClientFieldsYieldsValidationError() {
        var client = ClientDataFactory.createClient("invalidMail");
        client.setFirstName("");
        client.setEmail(null);
        clientRepository.save(client);
    }

    @DisplayName("Tests for an invalid email address and makes sure the validation fails")
    @Test(expected = TransactionSystemException.class)
    public void invalidEmailYieldsValidationError() {
        var client = ClientDataFactory.createClient("invalidMail");
        client.setEmail("invalid email address.");
        clientRepository.save(client);
    }

    public void canSaveValidClient() {
        var client = ClientDataFactory.createClient("valid");
        var savedClient = clientRepository.save(client);
        assertNotNull(savedClient.getId());
    }

    @DisplayName("Tests retrieving all clients with parameterized enabled/disabled property. Tests also paging capability")
    @Test
    public void canFindClientsByEnabledParamWithPaging() {
        // given .. one enabled client and 20 disabled ones
        var clientEnabled = ClientDataFactory.createClient("enabled");
        clientEnabled.setEnabled(true);
        clientRepository.save(clientEnabled);

        for (int i = 0; i < 20; i++) {
            var disabledClient = ClientDataFactory.createClient("disabled" + i);
            disabledClient.setEnabled(false);
            clientRepository.save(disabledClient);
        }

        // when (1)
        Page<Client> enabledClientsPage = clientRepository.findAllClientsWhereEnabledIs(PageRequest.of(0, 5), true);

        // then
        assertEquals(1, enabledClientsPage.getTotalElements());
        assertEquals(clientEnabled.getFirstName(), enabledClientsPage.getContent().get(0).getFirstName());

        // when (2)
        // comparison: find the disabled clients
        Page<Client> disabledClientsPage = clientRepository.findAllClientsWhereEnabledIs(PageRequest.of(0, 5), false);

        // then
        assertEquals(20, disabledClientsPage.getTotalElements());
        assertEquals(4, disabledClientsPage.getTotalPages());
    }

}
