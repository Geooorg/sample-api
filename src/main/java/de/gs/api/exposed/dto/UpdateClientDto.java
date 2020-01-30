package de.gs.api.exposed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientDto {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phoneNumber;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String address;
    @NotNull
    private String zipCode;
    @NotNull
    private String city;
    @NotNull
    private String country;

    private boolean enabled;

}
