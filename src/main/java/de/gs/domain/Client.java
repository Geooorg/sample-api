package de.gs.domain;

import lombok.Data;

@Data
public class Client {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private String zipCode;
    private String city;
    private String country;

    private boolean enabled;

}
