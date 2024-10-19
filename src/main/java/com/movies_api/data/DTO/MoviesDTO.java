package com.movies_api.data.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class MoviesDTO {
    private List<MovieDTO> movieList;
    private int totalElements;
    private int totalPages;
    private int currentPage;
    @JsonProperty("isFirst")
    private boolean isFirst;
    @JsonProperty("isLast")
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrev;

    public MoviesDTO(Page<MovieDTO> moviePage) {
        this.setMovieList(moviePage.getContent());
        this.setTotalElements(moviePage.getNumberOfElements());
        this.setTotalPages(moviePage.getTotalPages());
        this.setCurrentPage(moviePage.getNumber());
        this.setFirst(moviePage.isFirst());
        this.setLast(moviePage.isLast());
        this.setHasNext(moviePage.hasNext());
        this.setHasPrev(moviePage.hasPrevious());
    }
}