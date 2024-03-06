package br.com.acmeairlines;

import br.com.acmeairlines.controller.UserController;
import br.com.acmeairlines.domain.address.Address;
import br.com.acmeairlines.domain.address.AddressData;
import br.com.acmeairlines.domain.baggages.*;
import br.com.acmeairlines.domain.users.*;
import br.com.acmeairlines.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BaggageRepository baggageRepository;

    @MockBean
    private TokenService tokenService;

    @Test
    public void shouldNotAllowRequest() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "luis@gmail.com")
    public void shouldGetUser() throws Exception {
        UserDataRecord mockUser = new UserDataRecord(1L, "Luis", "luis@gmail.com", "999999999999",Role.REGULAR_USER, new Address());
        when(userRepository.findUserDataRecordByEmail("luis@gmail.com")).thenReturn(mockUser);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    @WithMockUser(username = "luis@gmail.com")
    public void shouldGetBaggage() throws Exception {
        UserDataRecord mockUser = new UserDataRecord(1L, "Luis", "luis@gmail.com", "999999999999",Role.REGULAR_USER, new Address());
        when(userRepository.findUserDataRecordByEmail("luis@gmail.com")).thenReturn(mockUser);

        BaggageModel mockBaggage = new BaggageModel(new BaggageRegisterData(1L,"ABC-123","Vermelha",7.5,"Despachada","Colocada no avião", 1L));
        when(baggageRepository.findById(1L)).thenReturn(Optional.of(mockBaggage));

        mockMvc.perform(get("/users/baggage/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "luis@gmail.com")
    public void shouldAddBaggage() throws Exception {
        BaggageUpdateData updateData = new BaggageUpdateData(1L, "Despachada", "Indo para o aviao", 1L);
        BaggageUpdateRequest updateRequest = new BaggageUpdateRequest();
        updateRequest.setData(updateData);
        updateRequest.setTag("ABC-123");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(updateRequest);

        BaggageModel existingBaggage = new BaggageModel();
        when(baggageRepository.findByTag("ABC-123")).thenReturn(existingBaggage);

        mockMvc.perform(post("/users/add/baggage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "luis@gmail.com")
    public void shouldUpdateUser() throws Exception {
        UserDataRecord mockUser = new UserDataRecord(1L, "Luis", "luis@gmail.com", "999999999999",Role.REGULAR_USER, new Address());
        when(userRepository.findUserDataRecordByEmail("luis@gmail.com")).thenReturn(mockUser);

        UserUpdateData uData = new UserUpdateData("Louis","luis@gmail.com", "123","999999999999", new AddressData("","","","","","",""));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(uData);

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
