package de.gs.application;

import de.gs.domain.Client;
import de.gs.persistence.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindClientService {

    private final ClientRepository clientRepository;

    public Page<Client> findAllClientsWhereEnabledIs(boolean enabled, int page, int pageSize) {
        return clientRepository.findAllClientsWhereEnabledIs(PageRequest.of(page, pageSize), enabled);
    }

    public Optional<Client> findById(String uuid) {
        return clientRepository.findById(uuid);
    }
}
