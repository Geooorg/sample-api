package de.gs.api.auth;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    private String username;
    private String password;
}
