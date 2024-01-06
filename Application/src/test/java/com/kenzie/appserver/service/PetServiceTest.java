package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.InteractionResponse;
import com.kenzie.appserver.controller.model.PetResponse;
import com.kenzie.appserver.repositories.PetRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.PetRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void adoptPet_ValidData_ReturnsPetResponse() {
        // GIVEN
        String petId = "123";
        String email = "test@example.com";
        String password = "password";

        UserRecord userRecord = new UserRecord();
        userRecord.setEmail(email);
        userRecord.setPassword(password);

        PetRecord petRecord = new PetRecord(petId, email);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(userRecord);
        Mockito.when(userRepository.save(userRecord)).thenReturn(userRecord);
        Mockito.when(petRepository.save(petRecord)).thenReturn(petRecord);

        // WHEN
        PetResponse result = petService.adoptPet(petId, email);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Long.parseLong(petId), result.getPetId());
        Assertions.assertEquals(email, result.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(userRepository, Mockito.times(1)).save(userRecord);
        Mockito.verify(petRepository, Mockito.times(1)).save(petRecord);
    }

    @Test
    void playWithPet_PetExists_ReturnsInteractionResponse() {
        // GIVEN
        String petId = "123";
        String email = "test@example.com";

        PetRecord petRecord = new PetRecord(petId, email);

        Mockito.when(petRepository.findByPetIdAndEmail(petId, email)).thenReturn(Optional.of(petRecord));
        Mockito.when(petRepository.save(petRecord)).thenReturn(petRecord);

        // WHEN
        InteractionResponse result = petService.playWithPet(petId, email);

        // THEN
        Assertions.assertNotNull(result);
        // Verify the expected changes in pet stats based on the interaction
        Assertions.assertEquals(petRecord.getHappinessLevel() - 2, result.getHappinessLevel());
        Assertions.assertEquals(petRecord.getEnergyLevel() + 2, result.getEnergyLevel());

        Mockito.verify(petRepository, Mockito.times(1)).findByPetIdAndEmail(petId, email);
        Mockito.verify(petRepository, Mockito.times(1)).save(petRecord);
    }

    @Test
    void groomPet_PetExists_ReturnsInteractionResponse() {
        // GIVEN
        String petId = "123";
        String email = "test@example.com";

        PetRecord petRecord = new PetRecord(petId, email);

        Mockito.when(petRepository.findByPetIdAndEmail(petId, email)).thenReturn(Optional.of(petRecord));
        Mockito.when(petRepository.save(petRecord)).thenReturn(petRecord);

        // WHEN
        InteractionResponse result = petService.groomPet(petId, email);

        // THEN
        Assertions.assertNotNull(result);
        // Verify the expected changes in pet stats based on the interaction
        Assertions.assertEquals(petRecord.getCleanlinessLevel() + 3, result.getCleanlinessLevel());

        Mockito.verify(petRepository, Mockito.times(1)).findByPetIdAndEmail(petId, email);
        Mockito.verify(petRepository, Mockito.times(1)).save(petRecord);
    }

    @Test
    void feedPet_PetExists_ReturnsInteractionResponse() {
        // GIVEN
        String petId = "123";
        String email = "test@example.com";

        PetRecord petRecord = new PetRecord(petId, email);

        Mockito.when(petRepository.findByPetIdAndEmail(petId, email)).thenReturn(Optional.of(petRecord));
        Mockito.when(petRepository.save(petRecord)).thenReturn(petRecord);

        // WHEN
        InteractionResponse result = petService.feedPet(petId, email);

        // THEN
        Assertions.assertNotNull(result);
        // Verify the expected changes in pet stats based on the interaction
        Assertions.assertEquals(petRecord.getHungerLevel() + 2, result.getHungerLevel());

        Mockito.verify(petRepository, Mockito.times(1)).findByPetIdAndEmail(petId, email);
        Mockito.verify(petRepository, Mockito.times(1)).save(petRecord);
    }

    @Test
    void getPetById_PetExists_ReturnsPetResponse() {
        // GIVEN
        String petId = "123";

        PetRecord petRecord = new PetRecord(petId);

        Mockito.when(petRepository.findByPetIdAndEmail(petId, null)).thenReturn(Optional.of(petRecord));
        Mockito.when(petRepository.save(ArgumentMatchers.any(PetRecord.class))).thenReturn(petRecord);

        // WHEN
        PetResponse result = petService.getPetById(petId);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Long.parseLong(petId), result.getPetId());

        Mockito.verify(petRepository, Mockito.times(1)).findByPetIdAndEmail(petId, null);
        Mockito.verify(petRepository, Mockito.times(1)).save(ArgumentMatchers.any(PetRecord.class));
    }

    @Test
    void triggerRandomEvent_PetExists_ReturnsInteractionResponse() {
        // GIVEN
        String petId = "123";

        PetRecord petRecord = new PetRecord(petId);

        Mockito.when(petRepository.findByPetId(petId)).thenReturn(petRecord);
        Mockito.when(petRepository.save(petRecord)).thenReturn(petRecord);

        // WHEN
        InteractionResponse result = petService.triggerRandomEvent(petId);

        // THEN
        Assertions.assertNotNull(result);

        // Verify that pet stats have been updated based on the triggered event
        Assertions.assertNotEquals(0, result.getHappinessLevel());
        Assertions.assertNotEquals(0, result.getEnergyLevel());
        Assertions.assertNotEquals(0, result.getHungerLevel());
        Assertions.assertNotEquals(0, result.getCleanlinessLevel());
        Assertions.assertNotEquals(0, result.getHealthLevel());
        Assertions.assertNotEquals(0, result.getObedienceLevel());

        Mockito.verify(petRepository, Mockito.times(1)).findByPetId(petId);
        Mockito.verify(petRepository, Mockito.times(1)).save(petRecord);
    }

    @Test
    void getPetStats_PetExists_ReturnsInteractionResponse() {
        // GIVEN
        String petId = "123";

        PetRecord petRecord = new PetRecord(petId);

        Mockito.when(petRepository.findByPetId(petId)).thenReturn(petRecord);

        // WHEN
        InteractionResponse result = petService.getPetStats(petId);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(petRecord.getPetId(), result.getPetId());

        Mockito.verify(petRepository, Mockito.times(1)).findByPetId(petId);
    }

    @Test
    void deletePet_UserExists_ReturnsInteractionResponse() {
        // GIVEN
        String petId = "123";
        String email = "test@example.com";

        UserRecord userRecord = new UserRecord();
        userRecord.setEmail(email);
        userRecord.setAdoptedPetId(petId);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(userRecord);
        Mockito.doNothing().when(petRepository).deleteByPetId(petId);
        Mockito.when(userRepository.save(userRecord)).thenReturn(userRecord);

        // WHEN
        InteractionResponse result = petService.deletePet(petId, email);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Pet deleted successfully.", result.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(petRepository, Mockito.times(1)).deleteByPetId(petId);
        Mockito.verify(userRepository, Mockito.times(1)).save(userRecord);
    }
}

