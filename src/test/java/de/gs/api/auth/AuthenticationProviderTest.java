package de.gs.api.auth;


import de.gs.api.auth.ApiUser;
import de.gs.api.auth.ApiUserRole;
import de.gs.api.auth.AuthenticationProvider;
import org.junit.Test;

import java.util.Set;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationProviderTest {

    private static final String secret = "verysecret";
    private AuthenticationProvider subject = new AuthenticationProvider(secret);

    @Test
    public void canEncodeAndValidateJwt() {
        String jwt = subject.createJwtForApiUser(
                ApiUser.builder().username("test").roles(Set.of(ApiUserRole.GUEST)).build()
        );

        assertNotNull(jwt);
        assertTrue(subject.isValid(jwt));
    }

    @Test(expected = RuntimeException.class)
    public void userWithoutRolesNotAllowed() {
        String jwt = subject.createJwtForApiUser(
                ApiUser.builder().username("test").roles(emptySet()).build()
        );

        assertNotNull(jwt);
        assertTrue(subject.isValid(jwt));
    }

}
