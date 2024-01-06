package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.appserver.controller.model.InteractionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "Pet")
public class PetRecord {

    private String petId;
    private String email;
    private int hungerLevel;
    private int happinessLevel;
    private int healthLevel;
    private int cleanlinessLevel;
    private int energyLevel;
    private int obedienceLevel;
    private int overallStatus;
    private List<String> attire = new ArrayList<>();

    public PetRecord(String petId, String email) {
        this.petId = petId;
        this.email = email;
    }

    public PetRecord() {
    }

    @DynamoDBHashKey(attributeName = "PetId")
    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    @DynamoDBAttribute(attributeName = "Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "HungerLevel")
    public int getHungerLevel() {
        return hungerLevel;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = validateLevel(hungerLevel);
    }

    @DynamoDBAttribute(attributeName = "HappinessLevel")
    public int getHappinessLevel() {
        return happinessLevel;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = validateLevel(happinessLevel);
    }

    @DynamoDBAttribute(attributeName = "HealthLevel")
    public int getHealthLevel() {
        return healthLevel;
    }

    public void setHealthLevel(int healthLevel) {
        this.healthLevel = validateLevel(healthLevel);
    }

    @DynamoDBAttribute(attributeName = "CleanlinessLevel")
    public int getCleanlinessLevel() {
        return cleanlinessLevel;
    }

    public void setCleanlinessLevel(int cleanlinessLevel) {
        this.cleanlinessLevel = validateLevel(cleanlinessLevel);
    }

    @DynamoDBAttribute(attributeName = "EnergyLevel")
    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = validateLevel(energyLevel);
    }

    @DynamoDBAttribute(attributeName = "ObedienceLevel")
    public int getObedienceLevel() {
        return obedienceLevel;
    }

    public void setObedienceLevel(int obedienceLevel) {
        this.obedienceLevel = validateLevel(obedienceLevel);
    }

    @DynamoDBAttribute(attributeName = "OverallStatus")
    public int getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(int overallStatus) {
        this.overallStatus = validateLevel(overallStatus);
    }

    @DynamoDBAttribute(attributeName = "Attire")
    public List<String> getAttire() {
        return attire;
    }

    public void setAttire(List<String> attire) {
        this.attire = attire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PetRecord petRecord = (PetRecord) o;
        return Objects.equals(petId, petRecord.petId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId);
    }

    @Override
    public String toString() {
        return "PetRecord{" +
                "petId='" + petId + '\'' +
                ", email='" + email + '\'' +
                ", hungerLevel=" + hungerLevel +
                ", happinessLevel=" + happinessLevel +
                ", healthLevel=" + healthLevel +
                ", cleanlinessLevel=" + cleanlinessLevel +
                ", energyLevel=" + energyLevel +
                ", obedienceLevel=" + obedienceLevel +
                ", overallStatus=" + overallStatus +
                ", attire=" + attire +
                '}';
    }

    public void updateStats(int happinessEffect, int energyEffect, int hungerEffect, int cleanlinessEffect,
                            int healthEffect, int obedienceEffect) {
        happinessLevel = clamp(happinessLevel + happinessEffect);
        energyLevel = clamp(energyLevel + energyEffect);
        hungerLevel = clamp(hungerLevel + hungerEffect);
        cleanlinessLevel = clamp(cleanlinessLevel + cleanlinessEffect);
        healthLevel = clamp(healthLevel + healthEffect);
        obedienceLevel = clamp(obedienceLevel + obedienceEffect);
        overallStatus = calculateOverallStatus();
    }

    public void decreaseStats() {
        happinessLevel = clamp(happinessLevel - 3);
        energyLevel = clamp(energyLevel - 4);
        hungerLevel = clamp(hungerLevel - 7);
        cleanlinessLevel = clamp(cleanlinessLevel - 5);
        healthLevel = clamp(healthLevel - 3);
        obedienceLevel = clamp(obedienceLevel - 6);
        overallStatus = calculateOverallStatus();
    }

    private int clamp(int value) {
        return Math.min(Math.max(value, 0), 100);
    }

    private int calculateOverallStatus() {
        int sum = happinessLevel + energyLevel + hungerLevel + cleanlinessLevel + healthLevel + obedienceLevel;
        return clamp(sum / 6);
    }

    private int validateLevel(int level) {
        return clamp(level);
    }

    public void setStats(InteractionResponse petStats) {
        setHungerLevel(petStats.getHungerLevel());
        setHappinessLevel(petStats.getHappinessLevel());
        setHealthLevel(petStats.getHealthLevel());
        setCleanlinessLevel(petStats.getCleanlinessLevel());
        setEnergyLevel(petStats.getEnergyLevel());
        setObedienceLevel(petStats.getObedienceLevel());
        setOverallStatus(petStats.getOverallStatus());
        setAttire(petStats.getAttire());
    }
}
