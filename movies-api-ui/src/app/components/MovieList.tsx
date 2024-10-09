"use client";
import { useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Movie from "./Movies"

interface MovieListProps {
  movies: Movie[];
}

export default function MovieList({ movies }: MovieListProps) {
  useEffect(() => {
    require('bootstrap/dist/js/bootstrap.bundle.min.js');
  }, []);

  return (
    <div className="accordion mt-3" id="accordionExample">
      {movies.map((movie, index) => (
        <div className="accordion-item" key={movie.id}>
          <h2 className="accordion-header" id={`heading${index}`}>
            <button
              className="accordion-button collapsed"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target={`#collapse${index}`}
              aria-expanded="false"
              aria-controls={`collapse${index}`}>
              {movie.title} ({movie.releaseYear})
            </button>
          </h2>
          <div
            id={`collapse${index}`}
            className="accordion-collapse collapse"
            aria-labelledby={`heading${index}`}
            data-bs-parent="#accordionExample">
            <div className="accordion-body">
              <strong>Movie description goes here.</strong>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}