package com.movies_api.api;

import com.movies_api.data.CreateMovieRequest;
import com.movies_api.data.MovieDTO;
import com.movies_api.data.MoviesDTO;
import com.movies_api.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
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

}