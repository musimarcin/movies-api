package com.movies_api.api;

import com.movies_api.data.repository.MovieRepo;
import com.movies_api.data.entity.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
/*
one method to set container*/
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/appdb"
})

//second method to set container (with @Container and @DynamicPropertySource)
@Testcontainers
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
    MovieRepo movieRepo;

    private List<Movie> movieList;
    @BeforeEach
    void addTestData() {
        movieRepo.deleteAllInBatch();
        movieList = new ArrayList<>();

        for (int i = 0; i < 30; i++)
            movieList.add(new Movie(null, "Movie " + i, 1988 + i, Instant.now()));

        movieRepo.saveAll(movieList);
    }
    //parameterized test with given inputs
    @ParameterizedTest
    @CsvSource({
            "1,10,3,0,true",
            "3,10,3,2,false"
    })
    void testGetMovies(int pageNo, int totalElements, int totalPages, int currentPage, boolean hasNext) throws Exception {
        mvc.perform(get("/api/movies?page="+pageNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.currentPage", equalTo(currentPage)))
                .andExpect(jsonPath("$.hasNext", equalTo(hasNext)));
    }

    //test with successful creation
    @Test
    void testCreateMovie() throws Exception {
        this.mvc.perform(
                        post("/api/movies")
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
        this.mvc.perform(
                        post("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "title": "Testing Movie 2"
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("releaseYear")))
                .andExpect(jsonPath("$.violations[0].message", is("Fill in release year")))
                .andReturn();
    }

    @Test
    void testCreateMovieWithoutTitle() throws Exception {
        this.mvc.perform(
                        post("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "releaseYear": 1888
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("title")))
                .andExpect(jsonPath("$.violations[0].message", is("Fill in title")))
                .andReturn();
    }

    @Test
    void testDeleteMovieExist() throws Exception {
        this.mvc.perform(
                delete("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "title" : "Movie 0",
                                        "releaseYear" : 1988
                                    }
                                """)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Movie deleted successfully"))
                .andReturn();
    }

    @Test
    void testDeleteMovieNotExist() throws Exception {
        this.mvc.perform(
                        delete("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "title" : "Movie 33",
                                        "releaseYear" : 1777
                                    }
                                """)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie not found"))
                .andReturn();
    }

    @Test
    void testDeleteMovieWithoutTitle() throws Exception {
        this.mvc.perform(
                delete("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "releaseYear" : 1777
                                    }
                                """)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title not provided"))
                .andReturn();
    }

    @Test
    void testDeleteMovieWithoutReleaseYear() throws Exception {
        this.mvc.perform(
                        delete("/api/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "title" : "Movie 0"
                                    }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Year not provided"))
                .andReturn();
    }
}