package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.PetCreateRequest;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register") // Update the annotation to handle POST requests
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserCreateRequest createRequest) {
        UserResponse userResponse = userService.registerUser(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        UserResponse userResponse = userService.loginUser(email, password);
        return ResponseEntity.ok(userResponse);
    }





    // Add other endpoints for updating user details, etc.
    @GetMapping("/logged-in-user/{email}")
    public ResponseEntity<UserResponse> getLoggedInUser(@PathVariable("email") String email) {
        UserResponse userResponse = userService.getLoggedInUser(email);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/update/{email}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("email") String email, @RequestBody UserCreateRequest updateRequest) {
        UserResponse userResponse = userService.updateUser(email, updateRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/logout/{email}")
    public ResponseEntity<String> logoutUser(@PathVariable("email") String email) {
        boolean logoutSuccessful = userService.logoutUser(email);
        if (logoutSuccessful) {
            return ResponseEntity.ok("Logout successful");
        } else {
            return ResponseEntity.badRequest().body("Logout failed");
        }
    }
}




