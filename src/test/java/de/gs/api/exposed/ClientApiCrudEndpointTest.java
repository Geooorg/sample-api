package de.gs.api.exposed;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gs.api.exposed.dto.UpdateClientDto;
import de.gs.application.FindClientService;
import de.gs.application.UpdateClientService;
import de.gs.domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static de.gs.api.exposed.ClientApiCrudEndpoint.*;
import static de.gs.testdata.ClientDataFactory.createClient;
import static de.gs.testdata.ClientDataFactory.createRandomNr;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientApiCrudEndpointTest {


    @Mock
    private FindClientService findClientService;
    @Mock
    private UpdateClientService updateClientService;

    private MockMvc mvc;


    @BeforeEach
    void setup() {
        ClientApiCrudEndpoint endpoint = new ClientApiCrudEndpoint(findClientService, updateClientService);
        mvc = MockMvcBuilders.standaloneSetup(endpoint).build();
    }


    @Test
    void canListAllClients() throws Exception {
        // given
        Client client1 = createClient(createRandomNr());
        Client client2 = createClient(createRandomNr());

        // when
        when(findClientService.findAllClientsWhereEnabledIs(true, 0, 10))
                .thenReturn(new PageImpl<>(List.of(client1, client2)));

        // then
        mvc.perform(get(CLIENT_API_PATH_LIST)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())

                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)))

                .andExpect(jsonPath("$.content.[0].id", is(client1.getId())))
                .andExpect(jsonPath("$.content.[0].firstName", startsWith("firstName_")))
                .andExpect(jsonPath("$.content.[0].email", notNullValue()))
                //  ...
                .andExpect(jsonPath("$.content.[1].id", is(client2.getId())))
                // ...
                .andExpect(jsonPath("$.content.[0].enabled", is(true)))
                .andExpect(jsonPath("$.content.[1].enabled", is(true)))

                .andReturn();
    }

    @Test
    void canSearchById() throws Exception {
        // given
        Client client1 = createClient(createRandomNr());

        // when
        when(findClientService.findById(client1.getId())).thenReturn(Optional.of(client1));

        // then
        mvc.perform(get(CLIENT_API_PATH_SEARCH_BY_ID, client1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(client1.getId())))
                .andExpect(jsonPath("$.firstName", startsWith("firstName_")))
                .andExpect(jsonPath("$.email", notNullValue()));
    }

    @Test
    void canAddClient() throws Exception {
        // given
        Client client = createClient(createRandomNr());
        UpdateClientDto dto = new UpdateClientDto();
        BeanUtils.copyProperties(client, dto);
        // Note: This does not replace an integration test as the desired behaviour is just mocked in this place!

        // when
        when(updateClientService.createClient(dto)).thenReturn(client.getId());
        when(findClientService.findById(client.getId())).thenReturn(Optional.of(client));

        // then
        mvc.perform(post(CLIENT_API_PATH_ADD_RESOURCE, client.getId())
                .content(new ObjectMapper().writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(client.getId())))
                .andExpect(jsonPath("$.firstName", startsWith("firstName_")))
                .andExpect(jsonPath("$.email", containsString("@sample.com")));
    }

    @Test
    void canUpdateClient() throws Exception {
        // given
        Client clientBefore = createClient(createRandomNr());
        UpdateClientDto dto = new UpdateClientDto();
        BeanUtils.copyProperties(clientBefore, dto);
        dto.setEmail("newEmail@sample.com");
        Client clientAfter = new Client();
        BeanUtils.copyProperties(dto, clientAfter);
        // Note: This does not replace an integration test as the desired behaviour is just mocked in this place!

        // when
        when(updateClientService.updateClient(dto, clientBefore.getId())).thenReturn(clientAfter);

        // then
        mvc.perform(put(CLIENT_API_PATH_UPDATE_RESOURCE, clientBefore.getId())
                .content(new ObjectMapper().writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", startsWith("firstName_")))
                .andExpect(jsonPath("$.email", is("newEmail@sample.com")));
    }


    @Test
    void canDeleteClient() throws Exception {
        // given
        Client client = createClient(createRandomNr());

        // then
        final MvcResult mvcResult = mvc.perform(delete(CLIENT_API_PATH_DELETE_RESOURCE, client.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = new String(mvcResult.getResponse().getContentAsByteArray());
        assertTrue(body.isEmpty());
    }




}