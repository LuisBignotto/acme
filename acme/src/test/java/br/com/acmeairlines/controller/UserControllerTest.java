package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.address.AddressData;
import br.com.acmeairlines.domain.baggages.*;
import br.com.acmeairlines.domain.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BaggageRepository baggageRepository;

    @InjectMocks
    private UserController userController;

    private MockHttpServletRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
    }

    @Test
    void getUser() {
        String email = "test@example.com";
        request.setRemoteUser(email);
        UserModel userModel = new UserModel("Test User", email, "encryptedPassword", Role.REGULAR_USER, true);

        UserDataRecord userDataRecord = new UserDataRecord(userModel);
        when(userRepository.findUserDataRecordByEmail(email)).thenReturn(userDataRecord);

        BaggageModel baggageModel = new BaggageModel();
        when(baggageRepository.findByUserId(userModel.getId())).thenReturn(List.of(baggageModel));

        ResponseEntity<UserAndBaggageResponse> response = userController.getUser(request);
        UserAndBaggageResponse userAndBaggageResponse = response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userModel.getName(), userAndBaggageResponse.getUser().name());
        assertEquals(userModel.getEmail(), userAndBaggageResponse.getUser().email());
        assertEquals(1, userAndBaggageResponse.getBaggage().size());
    }

    @Test
    void getBaggage() {
        String email = "test@example.com";
        Long baggageId = 1L;
        request.setRemoteUser(email);
        UserModel userModel = new UserModel("Test User", email, "encryptedPassword", Role.REGULAR_USER, true);
        userModel.setId(1L);

        BaggageModel baggageModel = new BaggageModel();
        baggageModel.setId(baggageId);
        baggageModel.setUserId(userModel.getId());

        when(userRepository.findUserDataRecordByEmail(email)).thenReturn(new UserDataRecord(userModel));
        when(baggageRepository.findById(baggageId)).thenReturn(Optional.of(baggageModel));

        ResponseEntity response = userController.getBaggage(request, baggageId);
        List<BaggageModel> baggages = (List<BaggageModel>) response.getBody();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, baggages.size());
        assertEquals(baggageId, baggages.get(0).getId());
        assertEquals(userModel.getId(), baggages.get(0).getUserId());
    }

    @Test
    void addBaggage() {
        String tag = "TestTag";
        BaggageUpdateData updateData = new BaggageUpdateData(
                1L,
                "Despachada",
                "Aeroporto Internacional de São Paulo",
                2L
        );
        BaggageUpdateRequest updateRequest = new BaggageUpdateRequest();
        updateRequest.setTag(tag);
        updateRequest.setData(updateData);

        BaggageModel existingBaggage = new BaggageModel();
        existingBaggage.setTag(tag);
        existingBaggage.setUserId(updateData.userId());
        existingBaggage.setStatus(updateData.status());
        existingBaggage.setLastSeenLocation(updateData.lastSeenLocation());
        existingBaggage.setFlightId(updateData.flightId());

        when(baggageRepository.findByTag(tag)).thenReturn(existingBaggage);

        ResponseEntity response = userController.addBaggage(updateRequest);

        assertEquals(200, response.getStatusCodeValue());
        BaggageModel updatedBaggage = (BaggageModel) response.getBody();
        assertEquals(tag, updatedBaggage.getTag());
        assertEquals(updateData.userId(), updatedBaggage.getUserId());
        assertEquals(updateData.status(), updatedBaggage.getStatus());
        assertEquals(updateData.lastSeenLocation(), updatedBaggage.getLastSeenLocation());
        assertEquals(updateData.flightId(), updatedBaggage.getFlightId());
    }

    @Test
    void updateUser() {
        String email = "test@example.com";
        request.setRemoteUser(email);
        UserModel existingUser = new UserModel("Test User", email, "encryptedPassword", Role.REGULAR_USER, true);
        existingUser.setId(1L);

        AddressData addressData = new AddressData(
                "Updated Street",
                "Updated Neighborhood",
                "12345",
                "123",
                "Updated Complement",
                "Updated City",
                "Updated State"
        );

        UserUpdateData updateData = new UserUpdateData(
                "Updated Name",
                "updated@example.com",
                "newEncryptedPassword",
                "1234567890",
                addressData
        );

        when(userRepository.findUserByEmail(email)).thenReturn(existingUser);

        ResponseEntity response = userController.updateUser(request, updateData);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteBag() {
        String email = "test@example.com";
        Long baggageId = 1L;
        request.setRemoteUser(email);
        UserModel userModel = new UserModel("Test User", email, "encryptedPassword", Role.REGULAR_USER, true);
        userModel.setId(1L);

        BaggageModel existingBaggage = new BaggageModel();
        existingBaggage.setId(baggageId);
        existingBaggage.setUserId(userModel.getId());

        when(userRepository.findUserDataRecordByEmail(email)).thenReturn(new UserDataRecord(userModel));
        when(baggageRepository.findById(baggageId)).thenReturn(Optional.of(existingBaggage));

        ResponseEntity response = userController.deleteBag(request, baggageId);

        assertEquals(204, response.getStatusCodeValue());
        verify(baggageRepository, times(1)).deleteById(baggageId);
    }

    @Test
    void deleteUser() {
        String email = "test@example.com";
        request.setRemoteUser(email);
        UserModel existingUser = new UserModel("Test User", email, "encryptedPassword", Role.REGULAR_USER, true);
        existingUser.setId(1L);

        when(userRepository.findUserByEmail(email)).thenReturn(existingUser);

        ResponseEntity response = userController.deleteUser(request);

        assertEquals(204, response.getStatusCodeValue());
        verify(userRepository, times(1)).delete(existingUser);
    }
}