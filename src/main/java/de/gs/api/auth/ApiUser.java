package de.gs.api.auth;

import lombok.Data;

import java.util.Set;

@Data
public class ApiUser {

    private String username;
    private Set<ApiUserRole> roles;
}
