package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kenzie.appserver.repositories.PetRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.PetRecord;
import com.kenzie.appserver.repositories.model.UserRecord;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

public class CustomizePetCreateRequest {

    @NotEmpty
    @JsonProperty("email")
    private String email;

    @NotEmpty
    @JsonProperty("attire")
    private List<String> attire;

    @NotEmpty
    @JsonProperty("petId")
    private String petId;

    PetRepository petRepository;

    UserRepository userRepository;

    public CustomizePetCreateRequest(String email, List<String> attire, String petId) {
        this.email = email;
        this.attire = attire;
        this.petId = petId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAttire() {
        return attire;
    }

    public void setAttire(List<String> attire) {
        this.attire = attire;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomizePetCreateRequest)) return false;
        CustomizePetCreateRequest that = (CustomizePetCreateRequest) o;
        return getEmail().equals(that.getEmail()) && getAttire().equals(that.getAttire()) && getPetId().equals(that.getPetId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getAttire(), getPetId());
    }

    @Override
    public String toString() {
        return "CustomizePetCreateRequest{" +
                "email='" + email + '\'' +
                ", attire='" + attire + '\'' +
                ", petId='" + petId + '\'' +
                '}';
    }

    private void saveAttire() {
     PetRecord pet = petRepository.findByPetId(petId);
        pet.setAttire(attire);
        petRepository.save(pet);
     UserRecord user = userRepository.findByAdoptedPetId(petId);
        user.setAttire(attire);
        userRepository.save(user);
    }
}