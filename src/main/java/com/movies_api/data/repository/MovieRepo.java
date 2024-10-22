package com.movies_api.data.repository;

import com.movies_api.data.DTO.MovieDTO;
import com.movies_api.data.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {


//    @Query("SELECT NEW com.movies_api.data.DTO.MovieDTO(m.id, m.title, m.releaseYear, m.addedWhen, m.userId) FROM Movie m")
//    Page<MovieDTO> findMovies(Pageable pageable);

    Page<MovieDTO> findByUserId(Long userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Movie m WHERE m.userId = :userId")
    void deleteTest(@Param("userId") Long userId);

    /* manually find by query
    @Query("""
    select new com.movies.movies_api.data.MovieDTO(m.id, m.title, m.releaseYear, m.addedWhen) from Movie m
    where lower(m.title) like lower(concat('%', :query, '%'))
    """)
    Page<MovieDTO> searchMovies(String query, Pageable pageable);
    */

    //spring boot method for finding by attribute Title in MovieDTO
    Page<MovieDTO> findByTitleContainingIgnoreCaseAndUserId(String query, Long userId, Pageable pageable);

    //with interface based projection
    //Page<MovieVM> findByTitleContainingIgnoreCase(String query, Pageable pageable);

    Optional<Movie> findByTitle(String title);
    Optional<Movie> findByTitleAndReleaseYear(String title, int releaseYear);
    Optional<Movie> findByTitleAndReleaseYearAndUserId(String string, int releaseYear, Long userId);

}
