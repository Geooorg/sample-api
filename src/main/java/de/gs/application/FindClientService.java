package de.gs.application;

import de.gs.api.exposed.dto.FindClientDto;
import de.gs.domain.Client;
import de.gs.persistence.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    // TODO check in how far Specification queries support Pagination
    public Page<Client>findClientsBySpecification(FindClientDto dto, int page, int pageSize) {
        log.debug("Searching for clients fulfilling the following properties: {}", dto);

        final Specification<Client> spec = new FindClientSpecification().findByProperties(dto);
        return clientRepository.findAll(spec, PageRequest.of(page, pageSize));
    }
}
