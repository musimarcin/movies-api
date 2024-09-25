package com.movies_api.data;

import com.movies_api.data.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {

    //for getting only specified columns
    @Query("select new com.movies_api.data.MovieDTO(m.id, m.title, m.releaseYear, m.addedWhen) from Movie m")
    Page<MovieDTO> findMovies(Pageable pageable);

    /* manually find by query
    @Query("""
    select new com.movies.movies_api.data.MovieDTO(m.id, m.title, m.releaseYear, m.addedWhen) from Movie m
    where lower(m.title) like lower(concat('%', :query, '%'))
    """)
    Page<MovieDTO> searchMovies(String query, Pageable pageable);
    */

    //spring boot method for finding by attribute Title in MovieDTO
    Page<MovieDTO> findByTitleContainingIgnoreCase(String query, Pageable pageable);

    //with interface based projection
    //Page<MovieVM> findByTitleContainingIgnoreCase(String query, Pageable pageable);

}
