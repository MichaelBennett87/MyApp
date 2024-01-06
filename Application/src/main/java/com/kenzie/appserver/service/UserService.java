package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.InteractionResponse;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.PetRepository;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.PetRecord;
import com.kenzie.appserver.repositories.model.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CacheStore userCache;
    private final CacheStore petCache;
    private final PetRepository petRepository;
    private final PetService petService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       CacheStore userCache, PetRepository petRepository, CacheStore petCache,
                       PetService petService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCache = userCache;
        this.petRepository = petRepository;
        this.petCache = petCache;
        this.petService = petService;
    }

    public UserResponse registerUser(UserCreateRequest createRequest) {
        validateUserCreateRequest(createRequest);
        checkEmailExists(createRequest.getEmail());

        UserRecord userRecord = new UserRecord();
        BeanUtils.copyProperties(createRequest, userRecord);
        userRecord.setPassword(passwordEncoder.encode(createRequest.getPassword()));

        UserRecord savedUserRecord = userRepository.save(userRecord);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(savedUserRecord, userResponse);

        userCache.addUser(userRecord.getEmail(), savedUserRecord);
        logger.info("User added to cache: {}", userRecord.getEmail());

        return userResponse;
    }

    public UserResponse loginUser(String email, String password) {
        validateEmailAndPassword(email, password);

        UserRecord userRecord = getUserByEmail(email);
        validateUserExists(userRecord);
        validatePasswordMatches(password, userRecord.getPassword());

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userRecord, userResponse);

        updateAdoptedPetStats(userRecord, userResponse);

        userCache.addUser(email, userRecord);
        logger.info("User added to cache: {}", email);

        return userResponse;
    }

    private void updateAdoptedPetStats(UserRecord userRecord, UserResponse userResponse) {
        String adoptedPetId = userRecord.getAdoptedPetId();
        if (adoptedPetId != null) {
            PetRecord adoptedPet = petCache.getPet(adoptedPetId);
            if (adoptedPet == null) {
                adoptedPet = petRepository.findByPetId(adoptedPetId);
                if (adoptedPet != null) {
                    InteractionResponse petStats = petService.getPetStats(adoptedPetId);
                    if (petStats != null) {
                        adoptedPet.setStats(petStats);
                        petCache.addPet(adoptedPetId, adoptedPet);
                        logger.info("Adopted pet added to cache: {}", adoptedPetId);
                    }
                }
            }
            assert adoptedPet != null;
            userRecord.setAdoptedPet(adoptedPet);
        }
    }



    private void validateUserCreateRequest(UserCreateRequest createRequest) {
        if (createRequest == null || createRequest.getEmail().isEmpty() || createRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Invalid UserCreateRequest");
        }
    }

    private void checkEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private void validateEmailAndPassword(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email and password are required");
        }
    }

    private UserRecord getUserByEmail(String email) {
        UserRecord userRecord = userCache.getUser(email);
        if (userRecord == null) {
            userRecord = userRepository.findByEmail(email);
            if (userRecord != null) {
                userCache.addUser(email, userRecord);
                logger.info("User added to cache: {}", email);
            }
        }
        return userRecord;
    }

    public String getEmailByAdoptedPetId(String petId) {
        PetRecord adoptedPet = petCache.getPet(petId);
        if (adoptedPet == null) {
            adoptedPet = petRepository.findByPetId(petId);
            if (adoptedPet != null) {
                petCache.addPet(petId, adoptedPet);
                logger.info("Adopted pet added to cache: {}", petId);
            }
        }
        assert adoptedPet != null;
        return adoptedPet.getEmail();

    }

    public UserResponse getLoggedInUser(String email) {
        UserRecord userRecord = userCache.getUser(email);
        if (userRecord == null) {
            userRecord = userRepository.findByEmail(email);
            if (userRecord != null) {
                userCache.addUser(email, userRecord);
                logger.info("User added to cache: {}", email);
            }
        }

        UserResponse userResponse = new UserResponse();
        assert userRecord != null;
        BeanUtils.copyProperties(userRecord, userResponse);

        updateAdoptedPetStats(userRecord, userResponse);

        return userResponse;
    }

    public UserResponse updateUser(String email, UserCreateRequest updateRequest) {
        UserRecord userRecord = userCache.getUser(email);
        if (userRecord == null) {
            userRecord = userRepository.findByEmail(email);
            if (userRecord != null) {
                userCache.addUser(email, userRecord);
                logger.info("User added to cache: {}", email);
            }
        }

        UserResponse userResponse = new UserResponse();
        assert userRecord != null;
        BeanUtils.copyProperties(userRecord, userResponse);

        updateAdoptedPetStats(userRecord, userResponse);

        return userResponse;
    }

    public boolean logoutUser(String email) {
        UserRecord userRecord = userCache.getUser(email);
        if (userRecord == null) {
            userRecord = userRepository.findByEmail(email);
            if (userRecord != null) {
                userCache.addUser(email, userRecord);
                logger.info("User added to cache: {}", email);
            }
        }

        if (userRecord != null) {
            userCache.removeUser(email);
            logger.info("User removed from cache: {}", email);
            return true;
        }
        return false;
    }

    private void validateUserExists(UserRecord userRecord) {
        if (userRecord == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    private void validatePasswordMatches(String password, String hashedPassword) {
        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}
