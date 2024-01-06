package com.kenzie.appserver.service;

import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.UserCreateRequest;
import com.kenzie.appserver.controller.model.UserResponse;
import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoderMock;

    @Mock
    private CacheStore userCache;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository, passwordEncoderMock, userCache);
    }

    @Test
    void registerUser_ValidUserCreateRequest_ReturnsUserResponse() {
        // GIVEN
        UserCreateRequest createRequest = new UserCreateRequest();
        createRequest.setEmail("test@example.com");
        createRequest.setPassword("password");

        UserRecord savedUserRecord = new UserRecord();
        savedUserRecord.setEmail(createRequest.getEmail());
        savedUserRecord.setPassword("hashedPassword");

        UserResponse expectedUserResponse = new UserResponse();
        expectedUserResponse.setEmail(savedUserRecord.getEmail());

        Mockito.when(userRepository.save(Mockito.any(UserRecord.class))).thenReturn(savedUserRecord);
        Mockito.when(passwordEncoderMock.encode(createRequest.getPassword())).thenReturn("hashedPassword");

        // WHEN
        UserResponse actualUserResponse = userService.registerUser(createRequest);

        // THEN
        Assertions.assertEquals(expectedUserResponse, actualUserResponse);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserRecord.class));
        Mockito.verify(userCache, Mockito.times(1)).addUser(Mockito.anyString(), Mockito.any(UserRecord.class));
    }

    @Test
    void loginUser_ValidCredentials_ReturnsUserResponse() {
        // GIVEN
        String email = "test@example.com";
        String password = "password";

        UserRecord userRecord = new UserRecord();
        userRecord.setEmail(email);
        userRecord.setPassword("hashedPassword");

        UserResponse expectedUserResponse = new UserResponse();
        BeanUtils.copyProperties(userRecord, expectedUserResponse);

        Mockito.when(userCache.getUser(email)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(userRecord);
        Mockito.when(passwordEncoderMock.matches(password, userRecord.getPassword())).thenReturn(true);

        // WHEN
        UserResponse actualUserResponse = userService.loginUser(email, password);

        // THEN
        Assertions.assertEquals(expectedUserResponse, actualUserResponse);
        Mockito.verify(userCache, Mockito.times(1)).getUser(email);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(userCache, Mockito.times(1)).addUser(email, userRecord);
    }

    @Test
    void loginUser_UserCached_ReturnsUserResponse() {
        // GIVEN
        String email = "test@example.com";
        String password = "password";

        UserRecord cachedUser = new UserRecord();
        cachedUser.setEmail(email);
        cachedUser.setPassword("hashedPassword");

        UserResponse expectedUserResponse = new UserResponse();
        BeanUtils.copyProperties(cachedUser, expectedUserResponse);

        Mockito.when(userCache.getUser(email)).thenReturn(cachedUser);
        Mockito.when(passwordEncoderMock.matches(password, cachedUser.getPassword())).thenReturn(true);

        // WHEN
        UserResponse actualUserResponse = userService.loginUser(email, password);

        // THEN
        Assertions.assertEquals(expectedUserResponse, actualUserResponse);
        Mockito.verify(userCache, Mockito.times(1)).getUser(email);
        Mockito.verify(userRepository, Mockito.never()).findByEmail(email);
        Mockito.verify(userCache, Mockito.never()).addUser(Mockito.anyString(), Mockito.any(UserRecord.class));
    }

    @Test
    void loginUser_InvalidCredentials_ThrowsIllegalArgumentException() {
        // GIVEN
        String email = "test@example.com";
        String password = "password";

        Mockito.when(userCache.getUser(email)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        // THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // WHEN
            userService.loginUser(email, password);
        });

        Mockito.verify(userCache, Mockito.times(1)).getUser(email);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(userCache, Mockito.never()).addUser(Mockito.anyString(), Mockito.any(UserRecord.class));
    }

    @Test
    void getLoggedInUser_UserExists_ReturnsUserResponse() {
        // GIVEN
        String email = "test@example.com";

        UserRecord userRecord = new UserRecord();
        userRecord.setEmail(email);
        userRecord.setPassword("hashedPassword");

        UserResponse expectedUserResponse = new UserResponse();
        BeanUtils.copyProperties(userRecord, expectedUserResponse);

        Mockito.when(userCache.getUser(email)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(userRecord);

        // WHEN
        UserResponse actualUserResponse = userService.getLoggedInUser(email);

        // THEN
        Assertions.assertEquals(expectedUserResponse, actualUserResponse);
        Mockito.verify(userCache, Mockito.times(1)).getUser(email);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(userCache, Mockito.times(1)).addUser(email, userRecord);
    }

    @Test
    void getLoggedInUser_UserCached_ReturnsUserResponse() {
        // GIVEN
        String email = "test@example.com";

        UserRecord cachedUser = new UserRecord();
        cachedUser.setEmail(email);
        cachedUser.setPassword("hashedPassword");

        UserResponse expectedUserResponse = new UserResponse();
        BeanUtils.copyProperties(cachedUser, expectedUserResponse);

        Mockito.when(userCache.getUser(email)).thenReturn(cachedUser);

        // WHEN
        UserResponse actualUserResponse = userService.getLoggedInUser(email);

        // THEN
        Assertions.assertEquals(expectedUserResponse, actualUserResponse);
        Mockito.verify(userCache, Mockito.times(1)).getUser(email);
        Mockito.verify(userRepository, Mockito.never()).findByEmail(email);
        Mockito.verify(userCache, Mockito.never()).addUser(Mockito.anyString(), Mockito.any(UserRecord.class));
    }

    @Test
    void getLoggedInUser_UserNotFound_ThrowsIllegalArgumentException() {
        // GIVEN
        String email = "test@example.com";

        Mockito.when(userCache.getUser(email)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        // THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // WHEN
            userService.getLoggedInUser(email);
        });

        Mockito.verify(userCache, Mockito.times(1)).getUser(email);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(userCache, Mockito.never()).addUser(Mockito.anyString(), Mockito.any(UserRecord.class));
    }

    @Test
    void updateUser_ValidRequest_ReturnsUserResponse() {
        // GIVEN
        String email = "test@example.com";
        UserCreateRequest updateRequest = new UserCreateRequest();
        updateRequest.setPassword("newPassword");

        UserRecord userRecord = new UserRecord();
        userRecord.setEmail(email);
        userRecord.setPassword("hashedPassword");

        UserRecord savedUserRecord = new UserRecord();
        savedUserRecord.setEmail(email);
        savedUserRecord.setPassword("newHashedPassword");

        UserResponse expectedUserResponse = new UserResponse();
        BeanUtils.copyProperties(savedUserRecord, expectedUserResponse);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(userRecord);
        Mockito.when(passwordEncoderMock.encode(updateRequest.getPassword())).thenReturn("newHashedPassword");
        Mockito.when(userRepository.save(userRecord)).thenReturn(savedUserRecord);

        // WHEN
        UserResponse actualUserResponse = userService.updateUser(email, updateRequest);

        // THEN
        Assertions.assertEquals(expectedUserResponse, actualUserResponse);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(passwordEncoderMock, Mockito.times(1)).encode(updateRequest.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(userRecord);
        Mockito.verify(userCache, Mockito.times(1)).addUser(email, savedUserRecord);
    }

    @Test
    void updateUser_UserNotFound_ThrowsIllegalArgumentException() {
        // GIVEN
        String email = "test@example.com";
        UserCreateRequest updateRequest = new UserCreateRequest();
        updateRequest.setPassword("newPassword");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        // THEN
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // WHEN
            userService.updateUser(email, updateRequest);
        });

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
        Mockito.verify(passwordEncoderMock, Mockito.never()).encode(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserRecord.class));
        Mockito.verify(userCache, Mockito.never()).addUser(Mockito.anyString(), Mockito.any(UserRecord.class));
    }

    @Test
    void getEmailByAdoptedPetId_UserFound_ReturnsEmail() {
        // GIVEN
        String adoptedPetId = "123";
        String email = "test@example.com";

        UserRecord userRecord = new UserRecord();
        userRecord.setEmail(email);
        userRecord.setPassword("hashedPassword");

        Mockito.when(userRepository.findByAdoptedPetId(adoptedPetId)).thenReturn(userRecord);

        // WHEN
        String actualEmail = userService.getEmailByAdoptedPetId(adoptedPetId);

        // THEN
        Assertions.assertEquals(email, actualEmail);
        Mockito.verify(userRepository, Mockito.times(1)).findByAdoptedPetId(adoptedPetId);
    }

    @Test
    void getEmailByAdoptedPetId_UserNotFound_ReturnsNull() {
        // GIVEN
        String adoptedPetId = "123";

        Mockito.when(userRepository.findByAdoptedPetId(adoptedPetId)).thenReturn(null);

        // WHEN
        String actualEmail = userService.getEmailByAdoptedPetId(adoptedPetId);

        // THEN
        Assertions.assertNull(actualEmail);
        Mockito.verify(userRepository, Mockito.times(1)).findByAdoptedPetId(adoptedPetId);
    }
}
