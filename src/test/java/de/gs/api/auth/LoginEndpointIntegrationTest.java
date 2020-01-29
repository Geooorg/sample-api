package de.gs.api.auth;

import de.gs.InitialDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

import static de.gs.api.auth.LoginEndpoint.LOGIN_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integrationtest"})
public class LoginEndpointIntegrationTest {

    String baseUrl;

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private InitialDataProvider initialDataProvider;


    @Before
    public void setup() {
        baseUrl = "http://localhost:" + localServerPort;
    }

    @Test
    public void canLoginUsingDefaultUser() {
        // given
        var adminUser = initialDataProvider.initialAdminUser();
        var loginDto = LoginDto.builder().username(adminUser.getUsername()).password(adminUser.getPassword()).build();

        // when
        var response = new RestTemplate().postForEntity(baseUrl + LOGIN_PATH, loginDto, String.class);

        // then
        assertEquals(response.getStatusCode().value(), HttpServletResponse.SC_OK);
    }

    @Test(expected = HttpClientErrorException.class)
    public void loginDeniedForUnknownUser() {
        // given
        var unknownUser = ApiUser.builder().username("unknown").password("any").build();
        var loginDto = LoginDto.builder().username(unknownUser.getUsername()).password(unknownUser.getPassword()).build();

        // when
        new RestTemplate().postForEntity(baseUrl + LOGIN_PATH, loginDto, String.class);
    }

}
