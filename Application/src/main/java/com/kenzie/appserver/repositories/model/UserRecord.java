package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;
@DynamoDBTable(tableName = "User")
public class UserRecord {

    private String id;
    private String name;
    private String email;
    private String password;
    private String adoptedPetId;
    private List<String> attire;

    public UserRecord(String id, String name, String email, String password, String adoptedPetId, List<String> attire) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.adoptedPetId = adoptedPetId;
        this.attire = attire;
    }

    public UserRecord() {

    }

    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    @DynamoDBAttribute(attributeName = "Email")
    public String getEmail() {
        return email;
    }

    @DynamoDBAttribute(attributeName = "Password")
    public String getPassword() {
        return password;
    }

    @DynamoDBAttribute(attributeName = "AdoptedPetId")
    public String getAdoptedPetId() {
        return adoptedPetId;
    }
    @DynamoDBAttribute(attributeName = "Attire")
    public List<String> getAttire() {
        return attire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRecord that = (UserRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserRecord{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", adoptedPetId='" + adoptedPetId + '\'' +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdoptedPetId(String petId) {
        this.adoptedPetId = petId;
    }

    public void setAdoptedPet(PetRecord adoptedPet) {
        this.adoptedPetId = adoptedPet.getPetId();
    }

    public void setPassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void save() {
    }

    public void setAttire(List<String> attire) {
        this.attire = attire;
    }

    // Optional: Implement a builder pattern for flexible object creation

    public static class Builder {
        private String id;
        private String name;
        private String email;
        private String password;
        private String adoptedPetId;
        private List<String> attire;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setAdoptedPetId(String adoptedPetId) {
            this.adoptedPetId = adoptedPetId;
            return this;
        }

        public Builder setAttire(List<String> attire) {
            this.attire = attire;
            return this;
        }

        public UserRecord build() {
            return new UserRecord(id, name, email, password, adoptedPetId, attire);
        }
    }
}