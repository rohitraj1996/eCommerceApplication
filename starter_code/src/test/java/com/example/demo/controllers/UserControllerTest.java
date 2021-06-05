package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        this.userController = new UserController();
        TestUtils.injectObject(this.userController, "userRepository", this.userRepository);
        TestUtils.injectObject(this.userController, "cartRepository", this.cartRepository);
        TestUtils.injectObject(this.userController, "bCryptPasswordEncoder", this.bCryptPasswordEncoder);
    }

    @Test
    public void createUserSuccessTest() {
        String username = "test";
        String password = "testPassword";

        when(this.bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = HelperControllerTest.createUserRequest(username, password);

        final ResponseEntity<User> response = this.userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void createUserFailTest() {
        String username = "test";
        String password = "pass";

        when(this.bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest user = HelperControllerTest.createUserRequest(username, password);

        final ResponseEntity<User> response = this.userController.createUser(user);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findByUsernameTest() {
        String username = "test";
        String password = "testPassword";

        when(this.bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = HelperControllerTest.createUserRequest(username, password);

        final ResponseEntity<User> userResponseEntity = this.userController.createUser(createUserRequest);

        when(this.userRepository.findByUsername(username)).thenReturn(userResponseEntity.getBody());

        final ResponseEntity<User> response = this.userController.findByUserName(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(userResponseEntity.getBody().getId(), user.getId());
        assertEquals(userResponseEntity.getBody().getUsername(), user.getUsername());
        assertEquals(userResponseEntity.getBody().getPassword(), user.getPassword());
    }

    @Test
    public void findByIdTest() {
        String username = "test";
        String password = "testPassword";

        when(this.bCryptPasswordEncoder.encode(password)).thenReturn("hashedPassword");

        CreateUserRequest createUserRequest = HelperControllerTest.createUserRequest(username, password);

        final ResponseEntity<User> userResponseEntity = this.userController.createUser(createUserRequest);

        when(this.userRepository.findById(0l)).thenReturn(Optional.of(userResponseEntity.getBody()));

        final ResponseEntity<User> response = this.userController.findById(0l);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(userResponseEntity.getBody().getId(), user.getId());
    }
}
