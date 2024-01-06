package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class InteractionRequest {
    @NotEmpty
    @JsonProperty("email")
    private String email;

    @NotEmpty
    @JsonProperty("password")
    private String password;

    @NotEmpty
    @JsonProperty("petId")
    private String petId;

    @Min(0)
    @Max(100)
    @JsonProperty("hungerLevel")
    private int hungerLevel;

    @Min(0)
    @Max(100)
    @JsonProperty("happinessLevel")
    private int happinessLevel;

    @Min(0)
    @Max(100)
    @JsonProperty("healthLevel")
    private int healthLevel;

    @Min(0)
    @Max(100)
    @JsonProperty("cleanlinessLevel")
    private int cleanlinessLevel;

    @Min(0)
    @Max(100)
    @JsonProperty("energyLevel")
    private int energyLevel;

    @Min(0)
    @Max(100)
    @JsonProperty("obedienceLevel")
    private int obedienceLevel;

    @Min(0)
    @Max(100)
    @JsonProperty("overallStatus")
    private int overallStatus;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = happinessLevel;
    }

    public int getHealthLevel() {
        return healthLevel;
    }

    public void setHealthLevel(int healthLevel) {
        this.healthLevel = healthLevel;
    }

    public int getCleanlinessLevel() {
        return cleanlinessLevel;
    }

    public void setCleanlinessLevel(int cleanlinessLevel) {
        this.cleanlinessLevel = cleanlinessLevel;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public int getObedienceLevel() {
        return obedienceLevel;
    }

    public void setObedienceLevel(int obedienceLevel) {
        this.obedienceLevel = obedienceLevel;
    }

    public int getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(int overallStatus) {
        this.overallStatus = overallStatus;
    }

    @Override
    public String toString() {
        return "InteractionRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", petId='" + petId + '\'' +
                ", hungerLevel=" + hungerLevel +
                ", happinessLevel=" + happinessLevel +
                ", healthLevel=" + healthLevel +
                ", cleanlinessLevel=" + cleanlinessLevel +
                ", energyLevel=" + energyLevel +
                ", obedienceLevel=" + obedienceLevel +
                ", overallStatus=" + overallStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InteractionRequest that = (InteractionRequest) o;
        return hungerLevel == that.hungerLevel &&
                happinessLevel == that.happinessLevel &&
                healthLevel == that.healthLevel &&
                cleanlinessLevel == that.cleanlinessLevel &&
                energyLevel == that.energyLevel &&
                obedienceLevel == that.obedienceLevel &&
                overallStatus == that.overallStatus &&
                email.equals(that.email) &&
                password.equals(that.password) &&
                petId.equals(that.petId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, petId, hungerLevel, happinessLevel, healthLevel, cleanlinessLevel, energyLevel, obedienceLevel, overallStatus);
    }


}