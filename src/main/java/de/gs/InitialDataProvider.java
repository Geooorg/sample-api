package de.gs;

import de.gs.api.auth.ApiUser;
import de.gs.api.auth.ApiUserRole;
import de.gs.persistence.ApiUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialDataProvider {

    private final ApiUserRepository apiUserRepository;

    @PostConstruct
    public void createAdminUserIfNotExists() {
        log.debug("Creating admin user (if it doesn't exist yet)");

        apiUserRepository.save(
                apiUserRepository.findByUsername("admin").orElse(
                        ApiUser.builder()
                                .username("admin")
                                .password("admin")
                                .roles(Set.of(ApiUserRole.CLIENT_MANAGER))
                                .build()
                ));
    }
}
