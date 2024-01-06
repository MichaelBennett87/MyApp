package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CustomizePetResponse;
import com.kenzie.appserver.repositories.PetRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.PetRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomizePetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;


    @Autowired
    public CustomizePetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    private String id;
    private String email;
    private List<String> attire;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void saveCustomPet(CustomizePetResponse response) {
        PetRecord petRecord = new PetRecord();
        petRecord.setPetId(response.getPetId());
        petRecord.setEmail(response.getEmail());
        petRecord.setAttire(response.getAttire());
        petRepository.save(petRecord);

        UserRecord userRecord = new UserRecord();
        userRecord.setAdoptedPetId(response.getPetId());
        userRecord.setEmail(response.getEmail());
        userRecord.setAttire(response.getAttire());
        userRepository.save(userRecord);
    }


    public CustomizePetResponse getCustomPet(String id) {
        PetRecord petRecord = petRepository.findByPetId(id);
        if (petRecord != null) {
            return new CustomizePetResponse(petRecord.getPetId(), petRecord.getEmail(), petRecord.getAttire());
        }
        return null;
    }


    public CustomizePetResponse createCustomPet(String petId, String email, List<String> attire) {
        CustomizePetResponse response = new CustomizePetResponse(petId, email, attire);
        saveCustomPet(response);
        return response;
    }
}