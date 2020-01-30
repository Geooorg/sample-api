package de.gs.testdata;

import de.gs.domain.Client;

public class ClientDataFactory {

    public static Client createClient(String identifier) {
        return Client.builder()
                .enabled(true)
                .firstName("firstName_" + identifier)
                .lastName("lastName_" + identifier)
                .address("Nice street " + identifier)
                .zipCode("12345" + identifier)
                .city("Citizen city " + identifier)
                .country("Space " + identifier)
                .email(identifier + "@sample.com")
                .phoneNumber("0049")
                .build();
    }
}
