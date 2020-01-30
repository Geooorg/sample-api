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

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integrationtest"})
class UpdateClientServiceIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    private UpdateClientService subject;

    @BeforeEach
    void cleanUp() {
        clientRepository.deleteAll();
        subject = new UpdateClientService(clientRepository);
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
}
