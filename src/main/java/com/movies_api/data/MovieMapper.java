package com.movies_api.data;


import com.movies_api.data.DTO.MovieDTO;
import com.movies_api.data.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieDTO toDTO(Movie movie) {
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseYear(),
                movie.getAddedWhen());
    }

    public Movie toEntity(MovieDTO movieDTO) {
        return new Movie(
                movieDTO.getId(),
                movieDTO.getTitle(),
                movieDTO.getReleaseYear(),
                movieDTO.getAddedWhen()
        );
    }

}
