package com.movies_api.api;

import com.movies_api.data.CreateMovieRequest;
import com.movies_api.data.MovieDTO;
import com.movies_api.data.MoviesDTO;
import com.movies_api.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    @Autowired
    private final MovieService movieService;

    @GetMapping
    public MoviesDTO getMovies(@RequestParam(name = "page", defaultValue = "1") Integer page,
                               @RequestParam(name = "query", defaultValue = "") String query) {
        if (query == null || query.length() == 0)
            return movieService.getMovies(page);
        else return movieService.searchMovies(query, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDTO createMovie(@RequestBody @Valid CreateMovieRequest request) {
        return movieService.createMovie(request);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMovie(@RequestBody HashMap<String, String> request) {
        String title = request.get("title");
        int releaseYear = request.get("releaseYear") == null ? -1 : Integer.parseInt(request.get("releaseYear"));
        if (releaseYear == -1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Year not provided");
        if (title == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Title not provided");
        boolean isDeleted = movieService.deleteMovie(title, releaseYear);
        if (isDeleted) return ResponseEntity.ok("Movie deleted successfully");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
    }

}