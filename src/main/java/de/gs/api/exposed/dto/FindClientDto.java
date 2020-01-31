package de.gs.api.exposed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindClientDto {

    private String id;
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
