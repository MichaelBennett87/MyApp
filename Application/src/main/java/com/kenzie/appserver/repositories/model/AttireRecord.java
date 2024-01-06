package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Objects;

/**
 * Represents an attire record stored in DynamoDB.
 */
@DynamoDBTable(tableName = "Attire")
public class AttireRecord<S> {

    private String id;
    private String name;
    private String type;
    private String petId;
    private String email;
    private List<String> attire;

    /**
    * Returns the ID of the attire.
     *
     * @return the attire ID
     */
    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the attire.
    *
     * @param id the attire ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the name of the attire.
     *
     * @return the attire name
     */
    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the attire.
     *
     * @param name the attire name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the attire.
     *
     * @return the attire type
     */
    @DynamoDBAttribute(attributeName = "Type")
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the attire.
     *
     * @param type the attire type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets whether the attire is used or not.
     *
     * @param isUsed true if the attire is used, false otherwise
     */
    public void setIsUsed(boolean isUsed) {
        if (isUsed) {
            setType("Used");
        } else {
            setType("Unused");
        }
    }

    /**
     * Sets the URL of the attire image.
     *
     * @param imageURL the URL of the attire image
     */
    public void setImageURL(String imageURL) {
        this.type = imageURL;
    }

    /**
     * Returns the URL of the attire image.
     *
     * @return the URL of the attire image
     */
    @DynamoDBAttribute(attributeName = "ImageURL")
    public String getImageURL() {
        return type;
    }

    /**
     * Returns whether the attire is used or not.
     *
     * @return true if the attire is used, false if unused
     */
    @DynamoDBAttribute(attributeName = "IsUsed")
    public boolean getIsUsed() {
        return type.equals("Used");
    }

    /**
     * Returns the pet ID associated with the attire.
     *
     * @return the pet ID
     */
    @DynamoDBAttribute(attributeName = "PetId")
    public String getPetId() {
        return petId;
    }
    /**
     * Sets the pet ID associated with the attire.
     *
     * @param petId the pet ID to set
     */
    public void setPetId(String petId) {
        this.petId = petId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttireRecord<S> that = (AttireRecord<S>) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(petId, that.petId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, petId);
    }

    @Override
    public String toString() {
        return "AttireRecord{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", petId=" + petId +
               '}';
    }


    public void setAttire(List<String> attire) {
        this.attire = attire;
    }

    public void save() {
        AttireRecord<String> attireRecord = new AttireRecord<>();
        attireRecord.setId(attireRecord.getId());
        attireRecord.setName(attireRecord.getName());
        attireRecord.setType(attireRecord.getType());
        attireRecord.setPetId(attireRecord.getPetId());
        attireRecord.setEmail(attireRecord.getEmail());
        attireRecord.setAttire(attireRecord.getAttire());
    }

    private List<String> getAttire() {
        return attire;
    }

    private String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }
}
