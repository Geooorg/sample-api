package de.gs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id",length = 32)

    @NotNull
    @Size(max = 30)
    private String firstName;

    @NotNull
    @Size(max = 60)
    private String lastName;

    @NotNull
    @Size(max = 30)
    private String phoneNumber;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(max = 100)
    private String address;

    @NotNull
    @Size(max = 10)
    private String zipCode;

    @NotNull
    @Size(max = 30)
    private String city;

    @NotNull
    @Size(max = 40)
    private String country;

    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

}
