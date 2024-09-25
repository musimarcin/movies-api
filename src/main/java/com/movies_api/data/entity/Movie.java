package com.movies_api.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "movies")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @SequenceGenerator(name = "movie_id_gen", sequenceName = "movie_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_id_gen")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer releaseYear;
    private Instant addedWhen;

}
