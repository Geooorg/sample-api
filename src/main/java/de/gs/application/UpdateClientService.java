package de.gs.application;

import de.gs.api.exposed.dto.UpdateClientDto;
import de.gs.domain.Client;
import de.gs.persistence.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateClientService {

    private final ClientRepository clientRepository;

    public String createClient(UpdateClientDto dto) {
        Client newClient = new Client();

        BeanUtils.copyProperties(dto, newClient);
        clientRepository.save(newClient);
        return newClient.getId();
    }

    public Client updateClient(UpdateClientDto dto, String id) throws IllegalArgumentException {
        Client clientToUpdate = clientRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Client not found with ID " + id));
        BeanUtils.copyProperties(dto, clientToUpdate);
        return clientRepository.save(clientToUpdate);
    }

    public void deleteClient(String id) throws IllegalArgumentException {
        Client clientToDelete = clientRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("Client not found with ID " + id));
        clientRepository.delete(clientToDelete);
    }
}
