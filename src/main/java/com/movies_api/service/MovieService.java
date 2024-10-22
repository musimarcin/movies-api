package com.movies_api.service;

import com.movies_api.data.DTO.CreateMovieRequest;
import com.movies_api.data.DTO.MovieDTO;
import com.movies_api.data.DTO.MoviesDTO;
import com.movies_api.data.entity.Movie;
import com.movies_api.data.entity.UserEntity;
import com.movies_api.data.repository.MovieRepo;
import com.movies_api.data.MovieMapper;
import com.movies_api.data.repository.UserRepo;
import com.movies_api.exceptions.ResourceNotFoundException;
import com.movies_api.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    @Autowired
    private final MovieRepo movieRepo;
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final MovieMapper movieMapper;

    public Pageable getPage(Integer page) {
        int pageNo = page < 1 ? 0 : page - 1;
        return PageRequest.of(pageNo, 10, Sort.Direction.ASC, "releaseYear");
    }

    private UserEntity getUser() {
        String username = SecurityUtil.getSessionUser();
        return userRepo.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public MoviesDTO getMovies(Integer page) {
        Long userId = getUser().getId();
        Page<MovieDTO> moviePage = movieRepo.findByUserId(userId, getPage(page));
        return new MoviesDTO(moviePage);
    }

    @Transactional(readOnly = true)
    public MoviesDTO searchMovies(String query, Integer page) {
        Long userId = getUser().getId();
        Page<MovieDTO> moviePage = movieRepo.findByTitleContainingIgnoreCaseAndUserId(query, userId, getPage(page));
        if (moviePage.isEmpty()) throw new ResourceNotFoundException("Movie with name " + query + " not found");
        return new MoviesDTO(moviePage);
    }

    public MovieDTO createMovie(CreateMovieRequest request) {
        Long userId = getUser().getId();
        Movie movie = new Movie(null, request.getTitle(), request.getReleaseYear(), Instant.now(), userId);
        Movie savedMovie = movieRepo.save(movie);
        return movieMapper.toDTO(savedMovie);
    }

    public boolean deleteMovie(String title, int releaseYear) {
        Long userId = getUser().getId();
        Optional<Movie> movie = movieRepo.findByTitleAndReleaseYearAndUserId(title, releaseYear, userId);
        if (movie.isPresent()) {
            movieRepo.delete(movie.get());
            return true;
        } else return false;
    }
}

