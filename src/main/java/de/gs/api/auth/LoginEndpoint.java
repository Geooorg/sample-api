package de.gs.api.auth;

import de.gs.persistence.ApiUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginEndpoint {

    static final String LOGIN_PATH = "/v1/login";

    private final ApiUserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;


    @PostMapping(value = LOGIN_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginDto loginDTO) {

        final String username = loginDTO.getUsername().trim();
        log.debug("Login attempt of user {}", username);
        Optional<ApiUser> userByLogin = userRepository.findByUsername(username);

        if (userByLogin.isEmpty() || userByLogin.get().getRoles().isEmpty()) {
            log.warn("No user found with login {} or no roles assigned", username);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ApiUser user = userByLogin.get();

        if (!(user.getPassword()).equals(loginDTO.getPassword())) {
            log.warn("Passwords do not match for user {}", username);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String jwt = authenticationProvider.createJwtForApiUser(user);
        log.info("User {} successfully logged in", user.getUsername());
        log.debug("JWT: {}", jwt);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

}
