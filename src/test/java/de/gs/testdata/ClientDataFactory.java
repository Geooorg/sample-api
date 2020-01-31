package de.gs.testdata;

import de.gs.domain.Client;

import java.util.Random;

public class ClientDataFactory {

    public static Client createClient(String identifier) {
        Client client = Client.builder()
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
        client.setId(identifier);
        return client;
    }

    /*
    public static List<Client> createListOfClients(int numberOfClients) {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < numberOfClients; i++) {
            clients.add(createClient(createRandomNr()));
        }
        return clients;
    }
     */

    public static String createRandomNr() {
        return String.valueOf(Math.abs(new Random().nextInt())).substring(0, 4);
    }

}
