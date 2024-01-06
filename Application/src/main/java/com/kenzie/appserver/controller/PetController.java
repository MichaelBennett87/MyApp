package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.repositories.PetRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.PetRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.PetService;
import com.kenzie.appserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final UserService userService;



    @Autowired
    public PetController(PetService petService, PetRepository petRepository , UserRepository userRepository, UserService userService  ) {
        this.petService = petService;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.userService = userService;


    }

    @PostMapping("/adopt/{petId}")
    public ResponseEntity<PetResponse> adoptPet(@PathVariable("petId") String petId, @RequestBody AdoptRequest adoptRequest) {
        PetResponse petResponse = petService.adoptPet(petId, adoptRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(petResponse);
    }

    @GetMapping("/getStats/{petId}")
    public ResponseEntity<InteractionResponse> getPetStats(@PathVariable("petId") String petId) {
        PetRecord petRecord = petRepository.findByPetId(petId);
        if (petRecord == null) {
            return ResponseEntity.notFound().build();
        }

        InteractionResponse interactionResponse = mapPetRecordToInteractionResponse(petRecord);
        return ResponseEntity.ok(interactionResponse);
    }

    @PostMapping("/setStats/{petId}")
    public ResponseEntity<InteractionResponse> setPetStats(@PathVariable("petId") String petId, @RequestBody InteractionRequest interactionRequest) {
        PetRecord petRecord = petRepository.findByPetId(petId);
        if (petRecord == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the pet stats
        petRecord.setHungerLevel(interactionRequest.getHungerLevel());
        petRecord.setHappinessLevel(interactionRequest.getHappinessLevel());
        petRecord.setHealthLevel(interactionRequest.getHealthLevel());
        petRecord.setCleanlinessLevel(interactionRequest.getCleanlinessLevel());
        petRecord.setEnergyLevel(interactionRequest.getEnergyLevel());
        petRecord.setObedienceLevel(interactionRequest.getObedienceLevel());
        petRecord.setOverallStatus(interactionRequest.getOverallStatus());

        // Save the updated pet record
        PetRecord updatedPetRecord = petRepository.save(petRecord);

        InteractionResponse interactionResponse = mapPetRecordToInteractionResponse(updatedPetRecord);
        return ResponseEntity.ok(interactionResponse);
    }

    @GetMapping("/get/{petId}")
    public ResponseEntity<PetResponse> getPet(@PathVariable("petId") String petId) {
        PetResponse petResponse = petService.getPetById(petId);
        return ResponseEntity.status(HttpStatus.CREATED).body(petResponse);
    }


    @PostMapping("/feed/{petId}")
    public ResponseEntity<InteractionResponse> feedPet(
            @PathVariable("petId") String petId, @RequestBody InteractionRequest interactionRequest
    ) {
        // Retrieve the email from the table based on the user information
        String email = userService.getEmailByAdoptedPetId(petId);

        if (email != null) {
            InteractionResponse interactionResponse = petService.feedPet(petId, email);
            return ResponseEntity.status(HttpStatus.CREATED).body(interactionResponse);
        } else {
            // Handle the case where the email is not found in the table
            // Return an appropriate response or error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new InteractionResponse());
        }
    }





    @PostMapping("/play/{petId}")
    public ResponseEntity<InteractionResponse> playPet(
            @PathVariable("petId") String petId, @RequestBody InteractionRequest interactionRequest
    ) {
        // Retrieve the email from the table based on the user information
        String email = userService.getEmailByAdoptedPetId(petId);

        if (email != null) {
            InteractionResponse interactionResponse = petService.playWithPet(petId, email);
            return ResponseEntity.status(HttpStatus.CREATED).body(interactionResponse);
        } else {
            // Handle the case where the email is not found in the table
            // Return an appropriate response or error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new InteractionResponse());
        }
    }


    @PostMapping("/groom/{petId}")
    public ResponseEntity<InteractionResponse> groomPet(
            @PathVariable("petId") String petId, @RequestBody InteractionRequest interactionRequest
    ) {
        // Retrieve the email from the table based on the user information
        String email = userService.getEmailByAdoptedPetId(petId);

        if (email != null) {
            InteractionResponse interactionResponse = petService.groomPet(petId, email);
            return ResponseEntity.status(HttpStatus.CREATED).body(interactionResponse);
        } else {
            // Handle the case where the email is not found in the table
            // Return an appropriate response or error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new InteractionResponse());
        }
    }




    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<InteractionResponse> deletePet(@PathVariable("petId") String petId, @RequestParam String email) {
        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            logger.info("Deleting pet with petId: {} for email: {}", petId, email);

            // Find the user record by email
            UserRecord userRecord = userRepository.findByEmail(email);
            if (userRecord == null) {
                logger.warn("User record not found for email: {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new InteractionResponse());
            }

            // Retrieve the adopted pet ID from the user record
            String adoptedPetId = userRecord.getAdoptedPetId();
            if (adoptedPetId == null || !adoptedPetId.equals(petId)) {
                logger.warn("Requested pet ID does not match the adopted pet ID for email: {}", email);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InteractionResponse());
            }

            // Delete the pet record from the pet repository
            petRepository.deleteByPetId(petId);

            // Clear the adopted pet ID from the user record
            userRecord.setAdoptedPetId(null);
            userRepository.save(userRecord);

            logger.info("Pet with petId: {} deleted successfully for email: {}", petId, email);

            InteractionResponse interactionResponse = new InteractionResponse();
            interactionResponse.setMessage("Pet deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(interactionResponse);
        } catch (Exception e) {
            logger.error("Failed to delete pet", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InteractionResponse());
        }
    }


    private InteractionResponse mapPetRecordToInteractionResponse(PetRecord petRecord) {
        InteractionResponse interactionResponse = new InteractionResponse();
        // Map the relevant information from the pet record to the InteractionResponse
        interactionResponse.setPetId(petRecord.getPetId());
        interactionResponse.setEmail(petRecord.getEmail());
        interactionResponse.setHungerLevel(petRecord.getHungerLevel());
        interactionResponse.setHappinessLevel(petRecord.getHappinessLevel());
        interactionResponse.setHealthLevel(petRecord.getHealthLevel());
        interactionResponse.setCleanlinessLevel(petRecord.getCleanlinessLevel());
        interactionResponse.setEnergyLevel(petRecord.getEnergyLevel());
        interactionResponse.setObedienceLevel(petRecord.getObedienceLevel());
        interactionResponse.setOverallStatus(petRecord.getOverallStatus());
        // Set other properties of the interactionResponse

        return interactionResponse;
    }


    // Exception handling for validation errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public String handleValidationException(org.springframework.validation.BindException ex) {
        return Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
    }
}