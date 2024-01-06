package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomizePetResponse {

    @JsonProperty("petId")
    private String petId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("attire")
    private List<String> attire;


    public CustomizePetResponse() {

    }
    public CustomizePetResponse(String petId, String email, List<String> attire) {
        this.petId = petId;
        this.email = email;
        this.attire = attire;
    }




    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomizePetResponse)) return false;
        CustomizePetResponse that = (CustomizePetResponse) o;
        return getPetId().equals(that.getPetId()) && getEmail().equals(that.getEmail()) && getAttire().equals(that.getAttire());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPetId(), getEmail(), getAttire());
    }

    @Override
    public String toString() {
        return "CustomizePetResponse{" +
                "petId=" + petId +
                ", email='" + email + '\'' +
                ", attire='" + attire + '\'' +
                '}';
    }
}