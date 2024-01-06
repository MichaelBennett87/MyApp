package com.kenzie.appserver.service.model;

import java.util.Objects;

public class Pet {
    private Long petId;
    private String email;

    // Constructors, getters, and setters

    public Pet() {
    }

    public Pet(String email) {
        this.email = email;
    }

    public Pet(Long petId, String email) {
        this.petId = petId;
        this.email = email;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(petId, pet.petId) && Objects.equals(email, pet.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId, email);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                ", email='" + email + '\'' +
                '}';
    }
}
