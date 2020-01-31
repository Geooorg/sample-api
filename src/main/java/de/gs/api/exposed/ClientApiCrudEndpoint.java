package de.gs.api.exposed;

import de.gs.api.exposed.dto.FindClientDto;
import de.gs.api.exposed.dto.UpdateClientDto;
import de.gs.application.FindClientService;
import de.gs.application.UpdateClientService;
import de.gs.domain.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientApiCrudEndpoint {

    private final FindClientService findClientService;
    private final UpdateClientService updateClientService;

    private final static String CLIENT_API_PATH = "/v1/client";

    final static String CLIENT_API_PATH_LIST = CLIENT_API_PATH + "/list";

    final static String CLIENT_API_PATH_ADD_RESOURCE = CLIENT_API_PATH;
    final static String CLIENT_API_PATH_UPDATE_RESOURCE = CLIENT_API_PATH + "/{id}";
    final static String CLIENT_API_PATH_DELETE_RESOURCE = CLIENT_API_PATH + "/{id}";


    final static String CLIENT_API_PATH_SEARCH_GENERIC = CLIENT_API_PATH + "/search/generic";
    final static String CLIENT_API_PATH_SEARCH_BY_ID = CLIENT_API_PATH + "/search/id/{id}";

    @GetMapping(value = CLIENT_API_PATH_LIST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Client>> listClients(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                    @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                    @RequestParam(name = "enabled", required = false, defaultValue = "true") boolean enabled) {

        log.debug("Returning all clients for page {} with pageSize {}", page, pageSize);
        Page<Client> foundClients = findClientService.findAllClientsWhereEnabledIs(enabled, page, pageSize);

        return new ResponseEntity<>(foundClients, OK);
    }

    @GetMapping(value = CLIENT_API_PATH_SEARCH_BY_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> searchClientById(@PathVariable("id") String id) {
        Optional<Client> optClient = findClientService.findById(id);

        if (optClient.isEmpty()) {
            log.debug("No client found with ID {}", id);
        }
        return new ResponseEntity<>(optClient.get(), OK);

    }

    @GetMapping(value = CLIENT_API_PATH_SEARCH_GENERIC, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Client>> searchClientsGeneric(@RequestBody FindClientDto findClientDto,
                                                             @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        return new ResponseEntity<>(findClientService.findClientsBySpecification(findClientDto, page, pageSize), OK);

    }

    @PostMapping(value = CLIENT_API_PATH_ADD_RESOURCE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> createClient(@RequestBody UpdateClientDto dto) {
        log.debug("Creating new client {}", dto);

        String createdId = updateClientService.createClient(dto);
        Client createdClient = findClientService.findById(createdId).orElseThrow(() -> new RuntimeException("Could not create and find entity"));

        return new ResponseEntity<>(createdClient, OK);
    }

    @PutMapping(value = CLIENT_API_PATH_UPDATE_RESOURCE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> updateClient(@RequestBody UpdateClientDto dto, @PathVariable("id") String id) {
        log.debug("Updating client with id {}: {}", id, dto);

        Client updatedClient = updateClientService.updateClient(dto, id);
        return new ResponseEntity<>(updatedClient, OK);
    }

    @DeleteMapping(value = CLIENT_API_PATH_DELETE_RESOURCE)
    public ResponseEntity<Client> deleteClient(@PathVariable("id") String id) {
        log.debug("Deleting client with id {}", id);

        updateClientService.deleteClient(id);
        return new ResponseEntity<>(OK);
    }
}
