package com.movies_api.controller;

import com.movies_api.data.entity.Role;
import com.movies_api.data.entity.UserEntity;
import com.movies_api.data.repository.RoleRepo;
import com.movies_api.data.repository.UserRepo;
import com.movies_api.security.JWTGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class UserControllerTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14-alpine"));

    @DynamicPropertySource
    static void propertiesSource(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    private String testUserToken;


    @BeforeEach
    void setUp() {
        Role role = roleRepo.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        UserEntity testUser = new UserEntity();
        testUser.setUsername("testUser");
        testUser.setPassword(passwordEncoder.encode("testUser"));
        testUser.setEmail("user@mail.com");
        testUser.setRoles(Collections.singletonList(role));
        userRepo.save(testUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                testUser.getUsername(),
                null,
                AuthorityUtils.createAuthorityList("USER"));

        this.testUserToken = jwtGenerator.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void cleanUp() {
        userRepo.deleteAllInBatch();
    }

    @Test
    void testRegisterSuccessfully() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "User",
                            "password": "User",
                            "email": "user2@mail.com"
                        }
                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered"));
    }

    @Test
    void testRegisterUsernameTaken() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "testUser",
                            "password": "testUser",
                            "email": "user@mail.com"
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username taken"));
    }

    @Test
    void testRegisterUsernameMissing() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "password": "User",
                            "email": "user2@mail.com"
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fields missing"));
    }

    @Test
    void testRegisterPasswordMissing() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "User",
                            "email": "user2@mail.com"
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fields missing"));
    }

    @Test
    void testRegisterEmailMissing() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "User",
                            "password": "User"
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fields missing"));
    }

    @Test
    void testRegisterUsernameEmpty() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "",
                            "password": "User",
                            "email": "user@mail.com"
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fields cannot be empty"));
    }

    @Test
    void testRegisterPasswordEmpty() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "User",
                            "password": "",
                            "email": "user@mail.com"
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fields cannot be empty"));
    }

    @Test
    void testRegisterEmailEmpty() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "User",
                            "password": "User",
                            "email": ""
                        }
                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fields cannot be empty"));
    }

    @Test
    void testRegisterAlreadyLoggedIn() throws Exception {
        this.mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "User",
                            "password": "User",
                            "email": "user2@mail.com"
                        }
                        """)
                )
                .andExpect(status().isForbidden())
                .andExpect(content().string("Already logged in"));
    }

    @Test
    void testLoginSuccessful() throws Exception {
        this.mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "testUser",
                            "password": "testUser"
                        }
                        """)
                )
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", matchesPattern("^token=.*")))
                .andExpect(content().string("Logged in successfully"));
    }

    @Test
    void testLoginUnsuccessful() throws Exception {
        this.mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "testUser",
                            "password": "test"
                        }
                        """)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credentials incorrect"));
    }

    @Test
    void testLogoutSuccessful() throws Exception {
        this.mvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", matchesPattern(".*Max-Age=0.*")))
                .andExpect(content().string("Logged out successfully"));
    }

    @Test
    void testDeleteUserSuccessful() throws Exception {
        this.mvc.perform(delete("/api/auth/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", matchesPattern(".*Max-Age=0.*")))
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    void testDeleteUserNotLoggedIn() throws Exception {
        SecurityContextHolder.clearContext();
        this.mvc.perform(delete("/api/auth/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("You are not logged in to delete user"));
    }


}