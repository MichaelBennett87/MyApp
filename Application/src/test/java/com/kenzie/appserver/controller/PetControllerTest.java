//package com.kenzie.appserver.controller;
//
//import com.kenzie.appserver.controller.model.AdoptRequest;
//import com.kenzie.appserver.controller.model.PetResponse;
//import com.kenzie.appserver.service.PetNotFoundException;
//import com.kenzie.appserver.service.PetService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//class PetControllerTest {
//
//    @Mock
//    private PetService petService;
//
//    private PetController petController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//        petController = new PetController(petService);
//    }
//
//    @Test
//    void adoptPet() {
//        // Prepare test data
//        String petId = "1";
//        AdoptRequest adoptRequest = new AdoptRequest();
//        PetResponse petResponse = new PetResponse();
//        // Mock the petService
//        when(petService.adoptPet(petId, adoptRequest.getEmail(), adoptRequest.getPassword()))
//                .thenReturn(petResponse);
//
//        // Invoke the method under test
//        ResponseEntity<PetResponse> responseEntity = petController.adoptPet(petId, adoptRequest);
//
//        // Verify the petService was called correctly
//        verify(petService, times(1)).adoptPet(petId, adoptRequest.getEmail(), adoptRequest.getPassword());
//
//        // Verify the response entity
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(petResponse, responseEntity.getBody());
//    }
//
//    @Test
//    void feedPet() throws PetNotFoundException {
//        // Prepare test data
//        Long petId = 1L;
//        PetResponse petResponse = new PetResponse();
//        // Mock the petService
//        when(petService.feedPet(petId)).thenReturn(petResponse);
//
//        // Invoke the method under test
//        ResponseEntity<PetResponse> responseEntity = petController.feedPet(petId);
//
//        // Verify the petService was called correctly
//        verify(petService, times(1)).feedPet(petId);
//
//        // Verify the response entity
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(petResponse, responseEntity.getBody());
//    }
//
//    @Test
//    void testGetPet() throws PetNotFoundException {
//        // Prepare test data
//        Long petId = 1L;
//        PetResponse expectedResponse = new PetResponse();
//        // Set up the mock service
//        when(petService.getPetById(String.valueOf(petId))).thenReturn(expectedResponse);
//
//        // Call the method under test
//        ResponseEntity<PetResponse> responseEntity = petController.getPet(String.valueOf(petId));
//
//        // Verify the results
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(expectedResponse, responseEntity.getBody());
//
//        // Verify the mock service was called correctly
//        verify(petService, times(1)).getPetById(String.valueOf(petId));
//    }
//}
//
