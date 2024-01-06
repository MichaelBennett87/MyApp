package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.repositories.PetRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.service.CustomizePetService;
import com.kenzie.appserver.service.PetService;
import com.kenzie.appserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customize")
public class CustomizePetController {

    private final PetService petService;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CustomizePetService customizePetService;

    @Autowired
    public CustomizePetController(PetService petService, PetRepository petRepository, UserRepository userRepository, UserService userService, CustomizePetService customizePetService) {
        this.petService = petService;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.customizePetService = customizePetService;
    }

    @PostMapping("/create/{petId}")
    public ResponseEntity<CustomizePetResponse> createPet(@PathVariable("petId") String petId, @RequestBody CustomizePetCreateRequest customizePetCreateRequest) {
        CustomizePetResponse customPetResponse = customizePetService.createCustomPet(petId, customizePetCreateRequest.getEmail(), customizePetCreateRequest.getAttire());
        return ResponseEntity.status(HttpStatus.CREATED).body(customPetResponse);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CustomizePetResponse> getPet(@PathVariable("id") String id) {
        CustomizePetResponse customPetResponse = customizePetService.getCustomPet(id);
        return ResponseEntity.status(HttpStatus.OK).body(customPetResponse);
    }
}

