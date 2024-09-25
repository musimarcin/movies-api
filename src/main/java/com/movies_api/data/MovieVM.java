package com.movies_api.data;

import java.time.Instant;

//interface based projection instead of DTO
public interface MovieVM {
    Long getId();
    String getTitle();
    Integer getreleaseYear();
    Instant getaddedWhen();
}
