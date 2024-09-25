package com.movies_api.data;


import com.movies_api.data.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieDTO toDTO(Movie movie) {
        return new MovieDTO(movie.getId(), movie.getTitle(), movie.getReleaseYear(), movie.getAddedWhen());
    }
}
