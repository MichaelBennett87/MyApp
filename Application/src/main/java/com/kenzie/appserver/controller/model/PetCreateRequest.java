package com.kenzie.appserver.controller.model;

import com.kenzie.appserver.repositories.model.PetRecord;
import com.kenzie.appserver.repositories.model.UserRecord;


public class PetCreateRequest {
    private String email;
    private String petId;

    public PetCreateRequest(String email, String petId) {
        this.email = email;
        this.petId = petId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public void savePetToUserAccount() {
        UserRecord userRecord = getUserRecord();
        PetRecord petRecord = getPetRecord();
        userRecord.setAdoptedPetId(petRecord.getPetId());
        // Save the updated UserRecord in the database
        saveUserRecord(userRecord);
    }

    private UserRecord getUserRecord() {
        // retrieve the UserRecord
        UserRecord userRecord = new UserRecord();
        userRecord.setEmail(userRecord.getEmail());
        userRecord.setAdoptedPetId(userRecord.getAdoptedPetId());
        userRecord.setPassword(userRecord.getPassword());
        userRecord.setName(userRecord.getName());
        return userRecord;
    }

    private PetRecord getPetRecord() {
        // retrieve the PetRecord
        PetRecord petRecord = new PetRecord();
        petRecord.setPetId(petRecord.getPetId());
        petRecord.setPetId(petRecord.getPetId());
        petRecord.setEmail(petRecord.getEmail());
        return petRecord;
    }

    private void saveUserRecord(UserRecord userRecord) {
        // logic to save the UserRecord in the database
        UserRecord userRecord1 = new UserRecord();
        userRecord1.setAdoptedPetId(userRecord.getAdoptedPetId());
        userRecord1.setEmail(userRecord.getEmail());
        userRecord1.setPassword(userRecord.getPassword());
        userRecord1.setName(userRecord.getName());
        userRecord1.setId(userRecord.getId());
        userRecord1.save();
    }
}
