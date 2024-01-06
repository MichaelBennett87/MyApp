package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class UserCreateRequest {

    @NotEmpty(message = "Name cannot be empty")
    @JsonProperty("name")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @JsonProperty("email")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @JsonProperty("password")
    private String password;

    // Add any other required fields for the create user request

    // Add getters and setters for the fields

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

    // Add getters and setters for any additional fields
}
