package de.gs.persistence;

import de.gs.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends CrudRepository<Client, String>, JpaSpecificationExecutor<Client> {

    @Query("select c from Client c where c.enabled = :enabled")
    Page<Client> findAllClientsWhereEnabledIs(Pageable pageable, @Param("enabled") boolean enabled);

}
