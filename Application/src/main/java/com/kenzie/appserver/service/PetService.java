package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.InteractionResponse;
import com.kenzie.appserver.controller.model.PetResponse;
import com.kenzie.appserver.repositories.PetRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.PetRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final CacheStore petCache;
    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    @Autowired
    public PetService(PetRepository petRepository, UserRepository userRepository, CacheStore petCache) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petCache = petCache;
        startUpdatingPetStats();
    }

    public PetResponse adoptPet(String petId, String email) {
        if (petId == null || petId.isEmpty()) {
            throw new IllegalArgumentException("Pet ID cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        UserRecord userRecord = userRepository.findByEmail(email);
        if (userRecord == null) {
            userRecord = new UserRecord();
        }

        if (userRecord.getAdoptedPetId() != null && userRecord.getAdoptedPetId().equals(petId)) {
            throw new IllegalArgumentException("You have already adopted this pet");
        }

        userRecord.setAdoptedPetId(petId);
        userRecord.setEmail(email);
        userRepository.save(userRecord);

        PetRecord petRecord = new PetRecord(petId, email);
        petRepository.save(petRecord);

        // Update cache
        petCache.addPet(petId, petRecord);
        logger.info("Pet updated successfully");

        return mapPetRecordToPetResponse(petRecord);
    }




    public InteractionResponse playWithPet(String petId, String email) {
        logger.info("Playing with pet. Pet ID: {}, Email: {}", petId, email);

        // Check cache first
        PetRecord petRecord = petCache.getPet(petId);
        logger.info(" pet record found: {}", petId);
        if (petRecord == null) {
            petRecord = petRepository.findByPetId(petId);
        }

        InteractionResponse response = updatePetStats(petRecord, email, 2, -2, 2, -2, 3, 3);

        // Update cache
        if (response != null) {
            petCache.updatePetStats(petId, petRecord);
            logger.info(" pet updated successfully ");
        }

        return response;
    }

    public InteractionResponse groomPet(String petId, String email) {
        logger.info("Grooming pet. Pet ID: {}, Email: {}", petId, email);

        // Check cache first
        PetRecord petRecord = petCache.getPet(petId);
        logger.info(" pet record found ");
        if (petRecord == null) {
            petRecord = petRepository.findByPetId(petId);
        }

        InteractionResponse response = updatePetStats(petRecord, email, 1, 3, 2, 2, 2, 3);

        // Update cache
        if (response != null) {
            petCache.updatePetStats(petId, petRecord);
            logger.info(" pet updated successfully ");
        }

        return response;
    }

    public InteractionResponse feedPet(String petId, String email) {
        logger.info("Feeding pet. Pet ID: {}, Email: {}", petId, email);

        // Check cache first
        PetRecord petRecord = petCache.getPet(petId);
        logger.info(" pet record found");
        if (petRecord == null) {
            petRecord = petRepository.findByPetId(petId);
        }

        InteractionResponse response = updatePetStats(petRecord, email, 3, 3, -2, -3, 3, 5);

        // Update cache
        if (response != null) {
            petCache.updatePetStats(petId, petRecord);
            logger.info(" pet updated successfully ");

        }

        return response;
    }

    public PetResponse getPetById(String petId) {
        // Check cache first
        PetRecord petRecord = Optional.ofNullable(petCache.getPet(petId))
                .orElseGet(() -> petRepository.findByPetId(petId));

        if (petRecord == null) {
            return null;
        }

        return mapPetRecordToPetResponse(petRecord);
    }


    public InteractionResponse triggerRandomEvent(String petId) {
        // Check cache first
        PetRecord petRecord = Optional.ofNullable(petCache.getPet(petId))
                .orElseGet(() -> petRepository.findByPetId(petId));

        if (petRecord == null) {
            return null;
        }

        Random random = new Random();
        int event = random.nextInt(4);

        int happinessEffect = 0;
        int energyEffect = 0;
        int hungerEffect = 0;
        int cleanlinessEffect = 0;
        int healthEffect = 0;
        int obedienceEffect = 0;

        switch (event) {
            case 0:
                happinessEffect = 2;
                energyEffect = 2;
                break;
            case 1:
                obedienceEffect = -2;
                cleanlinessEffect = -2;
                break;
            case 2:
                hungerEffect = -2;
                cleanlinessEffect = -2;
                break;
            case 3:
                obedienceEffect = 2;
                happinessEffect = -2;
                break;
        }

        InteractionResponse response = updatePetStats(petRecord, petRecord.getEmail(), happinessEffect, energyEffect,
                hungerEffect, cleanlinessEffect, healthEffect, obedienceEffect);

        // Update cache
        if (response != null) {
            petCache.updatePetStats(petId, petRecord);
            logger.info("Pet updated successfully");
        }

        return response;
    }


    public InteractionResponse getPetStats(String petId) {
        // Check cache first
        PetRecord petRecord = petCache.getPet(petId);
        logger.info(" pet record found");
        if (petRecord == null) {
            petRecord = petRepository.findByPetId(petId);
        }

        if (petRecord == null) {
            return null;
        }

        return mapPetRecordToInteractionResponse(petRecord);
    }



    public InteractionResponse deletePet(String petId, String email) {
        UserRecord userRecord = userRepository.findByEmail(email);
        if (userRecord == null) {
            return null;
        }

        String adoptedPetId = userRecord.getAdoptedPetId();
        if (adoptedPetId == null || !adoptedPetId.equals(petId)) {
            return null;
        }

        // Delete the petRecord
        petRepository.deleteByPetId(petId);

        // Remove the adoptedPetId from the userRecord
        userRecord.setAdoptedPetId(null);
        userRepository.save(userRecord);

        // Evict pet from cache
        petCache.evictPet(petId);

        logger.info("Pet deleted successfully");

        InteractionResponse response = new InteractionResponse();
        response.setMessage("Pet deleted successfully.");
        return response;
    }




    private InteractionResponse updatePetStats(PetRecord petRecord, String email, int happinessEffect, int energyEffect,
                                               int hungerEffect, int cleanlinessEffect, int healthEffect,
                                               int obedienceEffect) {
        if (petRecord != null) {
            petRecord.updateStats(happinessEffect, energyEffect, hungerEffect, cleanlinessEffect,
                    healthEffect, obedienceEffect);
            int overallStatus = calculateOverallStatus(petRecord);
            petRecord.setOverallStatus(overallStatus);

            // Update cache
            petCache.updatePetStats(petRecord.getPetId(), petRecord);

            // Save the updated pet record
            PetRecord savedPetRecord = petRepository.save(petRecord);

            // Log the update
            logger.info("Updated pet stats. Pet ID: {}, Email: {}", petRecord.getPetId(), email);

            return mapPetRecordToInteractionResponse(savedPetRecord);
        } else {
            return null;
        }
    }


    private InteractionResponse mapPetRecordToInteractionResponse(PetRecord petRecord) {
        InteractionResponse interactionResponse = new InteractionResponse();
        interactionResponse.setPetId(petRecord.getPetId());
        interactionResponse.setEmail(petRecord.getEmail());
        interactionResponse.setHappinessLevel(petRecord.getHappinessLevel());
        interactionResponse.setEnergyLevel(petRecord.getEnergyLevel());
        interactionResponse.setHungerLevel(petRecord.getHungerLevel());
        interactionResponse.setCleanlinessLevel(petRecord.getCleanlinessLevel());
        interactionResponse.setHealthLevel(petRecord.getHealthLevel());
        interactionResponse.setObedienceLevel(petRecord.getObedienceLevel());
        return interactionResponse;
    }

    private PetResponse mapPetRecordToPetResponse(PetRecord petRecord) {
        PetResponse petResponse = new PetResponse();
        String petIdString = petRecord.getPetId();

        try {
            UUID petId = UUID.fromString(petIdString);
            petResponse.setPetId(petId);
        } catch (IllegalArgumentException e) {
            // Handle the case when the petIdString cannot be parsed as a UUID
            // Log an error or throw an exception if needed
            // For now, setting petId to null
            petResponse.setPetId(null);
        }

        petResponse.setEmail(petRecord.getEmail());
        return petResponse;
    }

    public int calculateOverallStatus(PetRecord petRecord) {
        int happinessLevel = petRecord.getHappinessLevel();
        int energyLevel = petRecord.getEnergyLevel();
        int hungerLevel = petRecord.getHungerLevel();
        int cleanlinessLevel = petRecord.getCleanlinessLevel();
        int healthLevel = petRecord.getHealthLevel();
        int obedienceLevel = petRecord.getObedienceLevel();

        // Calculate the overallStatus as the average of the pet's stats

        return (happinessLevel + energyLevel + hungerLevel + cleanlinessLevel +
                healthLevel + obedienceLevel) / 6;
    }

    private void startUpdatingPetStats() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                decreasePetStats();
            }
        };
        timer.schedule(task, 0, 10000);
    }
    private void decreasePetStats() {
        petRepository.findAll().forEach(petRecord -> {
            petRecord.decreaseStats();
            petRepository.save(petRecord);
        });
    }
}
