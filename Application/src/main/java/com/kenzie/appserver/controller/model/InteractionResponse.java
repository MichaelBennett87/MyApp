package com.kenzie.appserver.controller.model;

import com.kenzie.appserver.repositories.model.PetRecord;

import java.util.Objects;

public class InteractionResponse extends PetRecord {
    public String response;
    public String petId;
    public String email;
    public int hungerLevel;
    public int happinessLevel;
    public int sleepLevel;
    public int healthLevel;
    public int cleanlinessLevel;
    public int energyLevel;
    public int obedienceLevel;
    public int overallStatus;

    public InteractionResponse() {
    }


    public String getResponse() {
        return response;
    }

    public String getEmail() {
        return email;
    }

    public String getPetId() {
        return petId;
    }


    public int getHungerLevel() {
        return hungerLevel;
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

    public int getSleepLevel() {
        return sleepLevel;
    }

    public int getHealthLevel() {
        return healthLevel;
    }

    public int getCleanlinessLevel() {
        return cleanlinessLevel;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public int getObedienceLevel() {
        return obedienceLevel;
    }

    public int getOverallStatus() {
        return overallStatus;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = happinessLevel;
    }

    public void setSleepLevel(int sleepLevel) {
        this.sleepLevel = sleepLevel;
    }

    public void setHealthLevel(int healthLevel) {
        this.healthLevel = healthLevel;
    }

    public void setCleanlinessLevel(int cleanlinessLevel) {
        this.cleanlinessLevel = cleanlinessLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public void setObedienceLevel(int obedienceLevel) {
        this.obedienceLevel = obedienceLevel;
    }

    public void setOverallStatus(int overallStatus) {
        this.overallStatus = overallStatus;
    }


    @Override
    public String toString() {
        return "InteractionResponse{" +
                "response='" + response + '\'' +
                ", petId='" + petId + '\'' +
                ", email='" + email + '\'' +
                ", hungerLevel=" + hungerLevel +
                ", happinessLevel=" + happinessLevel +
                ", sleepLevel=" + sleepLevel +
                ", healthLevel=" + healthLevel +
                ", cleanlinessLevel=" + cleanlinessLevel +
                ", energyLevel=" + energyLevel +
                ", obedienceLevel=" + obedienceLevel +
                ", overallStatus=" + overallStatus +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractionResponse that = (InteractionResponse) o;
        return Objects.equals(response, that.response) &&
                Objects.equals(petId, that.petId) &&
                Objects.equals(email, that.email) &&
                Objects.equals(hungerLevel, that.hungerLevel) &&
                Objects.equals(happinessLevel, that.happinessLevel) &&
                Objects.equals(sleepLevel, that.sleepLevel) &&
                Objects.equals(healthLevel, that.healthLevel) &&
                Objects.equals(cleanlinessLevel, that.cleanlinessLevel) &&
                Objects.equals(energyLevel, that.energyLevel) &&
                Objects.equals(obedienceLevel, that.obedienceLevel) &&
                Objects.equals(overallStatus, that.overallStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(response, petId, email, hungerLevel, happinessLevel, sleepLevel, healthLevel, cleanlinessLevel, energyLevel, obedienceLevel, overallStatus);
    }

    public void setMessage(String s) {
        response = s;
    }

    public String getMessage() {
        return response;

    }
}