package com.kenzie.appserver.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import java.util.Objects;

public class User {

    private Long id;
    private String name;
    private String email;
    private String password;

    @DynamoDBIgnore
    private Pet adoptedPet;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Pet getAdoptedPet() {
        return adoptedPet;
    }

    public void setAdoptedPet(Pet adoptedPet) {
        this.adoptedPet = adoptedPet;
    }

    // Override equals, hashCode, and toString methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(adoptedPet, user.adoptedPet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, adoptedPet);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", adoptedPet=" + adoptedPet +
                '}';
    }
}
