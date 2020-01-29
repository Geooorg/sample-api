package de.gs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableJpaRepositories
public class ClientApiApplication {

    public static void main(String[] args) {
        run(de.gs.ClientApiApplication.class, args);
    }


}
