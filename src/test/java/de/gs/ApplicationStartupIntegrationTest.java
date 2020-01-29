package de.gs;

import de.gs.persistence.ApiUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "integrationtest" })

public class ApplicationStartupIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ApiUserRepository userRepository;

    @Test
    public void applicationCanStart() {
        assertNotNull(applicationContext);
        assertNotNull(userRepository.findByUsername("admin"));
    }


}
