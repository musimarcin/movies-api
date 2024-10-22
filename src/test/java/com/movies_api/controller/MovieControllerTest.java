package com.movies_api.controller;

import com.movies_api.data.entity.Role;
import com.movies_api.data.entity.UserEntity;
import com.movies_api.data.repository.MovieRepo;
import com.movies_api.data.entity.Movie;
import com.movies_api.data.repository.RoleRepo;
import com.movies_api.data.repository.UserRepo;
import com.movies_api.security.JWTGenerator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
////one method to set container
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:postgresql://localhost:5432/appdb"
//})
//second method to set container (with @Container and @DynamicPropertySource)
@Testcontainers
@Transactional
class MovieControllerTest {

    //same as @TestPropertySource
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
    private MovieRepo movieRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String testUserToken;
    private Long testUserId;



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

        List<Movie> movieList = new ArrayList<>();
        for (int i = 0; i < 30; i++)
            movieList.add(new Movie(null, "Movie " + i, 1988 + i, Instant.now(), testUser.getId()));
        movieRepo.saveAll(movieList);

    }

    @AfterEach
    void cleanUp() {
        Long userId = userRepo.findByUsername("testUser").getId();
        movieRepo.deleteTest(userId);
        userRepo.delete(userRepo.findByUsername("testUser"));
    }


    //parameterized test with given inputs
    @ParameterizedTest
    @CsvSource({
            "1,10,3,0,true",
            "3,10,3,2,false"
    })
    void testGetMovies(int pageNo, int totalElements, int totalPages, int currentPage, boolean hasNext) throws Exception {
        mvc.perform(get("/api/movies?page=" + pageNo)
//                        .header("Authorization", "Bearer " + testUserToken)
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.currentPage", equalTo(currentPage)))
                .andExpect(jsonPath("$.hasNext", equalTo(hasNext)));
    }

    @ParameterizedTest
    @CsvSource({
            "Movie 4,1,1,0,false",
            "Movie 20,1,1,0,false"
    })
    void testGetQueryMovie(String query, int totalElements, int totalPages, int currentPage, boolean hasNext) throws Exception{
        mvc.perform(get("/api/movies?query=" + query))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", is("application/json")))
                .andExpect(jsonPath("$.movieList", hasSize(1)))
                .andExpect(jsonPath("$.movieList[0].id", notNullValue()))
                .andExpect(jsonPath("$.movieList[0].title", equalTo(query)))
                .andExpect(jsonPath("$.totalElements", equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.currentPage", equalTo(currentPage)))
                .andExpect(jsonPath("$.hasNext", equalTo(hasNext)));
    }

    @ParameterizedTest
    @ValueSource(strings = "Movie 33")
    void testGetQueryMoviesBadRequest(String query) throws Exception {
        mvc.perform(get("/api/movies?query=" + query))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", is("application/json")));
    }


    @Test
    public void testReturnUnauthorizedWhenNoToken() throws Exception {
        SecurityContextHolder.clearContext();
        mvc.perform(get("/api/movies"))
                .andExpect(status().isUnauthorized());
    }

    //test with successful creation
    @Test
    void testCreateMovie() throws Exception {
        this.mvc.perform(post("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "title": "Testing Movie",
                                    "releaseYear": 1777
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Testing Movie")))
                .andExpect(jsonPath("$.releaseYear", equalTo(1777)));
    }

    //test unsuccessful
    @Test
    void testCreateMovieWithoutYear() throws Exception {
        this.mvc.perform(post("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "title": "Testing Movie 2"
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/json")))
                .andExpect(jsonPath("$.releaseYear", is("Fill in release year")));
    }

    @Test
    void testCreateMovieWithoutTitle() throws Exception {
        this.mvc.perform(post("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "releaseYear": 1888
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/json")))
                .andExpect(jsonPath("$.title", is("Fill in title")));
    }

    @Test
    void testDeleteMovieExist() throws Exception {
        this.mvc.perform(delete("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title" : "Movie 0",
                                        "releaseYear" : 1988
                                    }
                                """)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Movie deleted successfully"));
    }

    @Test
    void testDeleteMovieNotExist() throws Exception {
        this.mvc.perform(delete("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "title" : "Movie 33",
                                        "releaseYear" : 1777
                                    }
                                """)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie not found"));
    }

    @Test
    void testDeleteMovieWithoutTitle() throws Exception {
        this.mvc.perform(delete("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "releaseYear" : 1777
                                    }
                                """)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title not provided"));
    }

    @Test
    void testDeleteMovieWithoutReleaseYear() throws Exception {
        this.mvc.perform(delete("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "title" : "Movie 0"
                                    }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Year not provided"));
    }
}