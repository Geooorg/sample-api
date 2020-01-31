package de.gs.application;


import de.gs.api.exposed.dto.FindClientDto;
import de.gs.domain.Client;
import org.h2.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

class FindClientSpecification {

    Specification<Client> findByProperties(FindClientDto dto) {
        Specification<Client> spec = allEnabledClients();

        // If "find by id" is desired, then even disabled clients should be returned and the result should be 'unique'
        if (!StringUtils.isNullOrEmpty(dto.getId())) {
            return (Specification<Client>) (path, query, criteriaBuilder) ->
                    criteriaBuilder.equal(path.get("id"), dto.getId().trim());
        }
        // ... same goes for the email field
        else if (!StringUtils.isNullOrEmpty(dto.getEmail())) {
            return (Specification<Client>) (path, query, criteriaBuilder) ->
                    criteriaBuilder.equal(path.get("email"), dto.getEmail().trim());
        }

        if (!StringUtils.isNullOrEmpty(dto.getFirstName())) {
            addAndContainsIgnoreCaseSpecification(spec, "firstName", dto.getFirstName());
        }

        if (!StringUtils.isNullOrEmpty(dto.getLastName())) {
            addAndContainsIgnoreCaseSpecification(spec, "lastName", dto.getLastName());
        }

        // .. TODO: add further filters for all the other properties

        return spec;
    }


    private Specification<Client> allEnabledClients() {
        return (Specification<Client>) (path, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(path.get("enabled"), Boolean.TRUE)
                );
    }

    private void addAndContainsIgnoreCaseSpecification(Specification<Client> spec, String property, String value) {
        spec.and(addAndContainsIgnoreCaseSpecification(property, value));
    }

    private Specification<Client> addAndContainsIgnoreCaseSpecification(String property, String value) {
        return (Specification<Client>) (path, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.like(path.get(property), "%" + value + "%")
                );
    }
}
