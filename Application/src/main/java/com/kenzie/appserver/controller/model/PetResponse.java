package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class PetResponse {

    @JsonProperty("petId")
    private UUID petId;

    @JsonProperty("email")
    private String email;

    // Constructors, getters, and setters

    public PetResponse() {
    }

    public PetResponse(UUID petId, String email) {
        this.petId = petId;
        this.email = email;
    }

    public UUID getPetId() {
        return petId;
    }

    public void setPetId(UUID petId) {
        this.petId = petId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}