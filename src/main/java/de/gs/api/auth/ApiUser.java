package de.gs.api.auth;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name="api_users",
        uniqueConstraints= @UniqueConstraint(columnNames={"username"})
)
public class ApiUser {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "username", length = 32)
    private String username;

    @Column(name = "password", length = 32)
    private String password;

    @ElementCollection
    private Set<ApiUserRole> roles;
}
