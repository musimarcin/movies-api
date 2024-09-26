package com.movies_api.service;

import com.movies_api.data.*;
import com.movies_api.data.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    @Autowired
    private final MovieRepo movieRepo;
    @Autowired
    private final MovieMapper movieMapper;

    public Pageable getPage(Integer page) {
        int pageNo = page < 1 ? 0 : page - 1;
        return PageRequest.of(pageNo, 10, Sort.Direction.ASC, "releaseYear");
    }

    @Transactional(readOnly = true)
    public MoviesDTO getMovies(Integer page) {
        Page<MovieDTO> moviePage = movieRepo.findMovies(getPage(page));
        return new MoviesDTO(moviePage);
    }

    @Transactional(readOnly = true)
    public MoviesDTO searchMovies(String query, Integer page) {
        Page<MovieDTO> moviePage = movieRepo.findByTitleContainingIgnoreCase(query, getPage(page));
        return new MoviesDTO(moviePage);
    }

    public MovieDTO createMovie(CreateMovieRequest request) {
        Movie movie = new Movie(null, request.getTitle(), request.getReleaseYear(), Instant.now());
        Movie savedMovie = movieRepo.save(movie);
        return movieMapper.toDTO(savedMovie);
    }

}

