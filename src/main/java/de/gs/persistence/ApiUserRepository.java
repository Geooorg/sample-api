package de.gs.persistence;

import de.gs.api.auth.ApiUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApiUserRepository extends CrudRepository<ApiUser, String> {

    Optional<ApiUser> findByUsername(String username);
}
