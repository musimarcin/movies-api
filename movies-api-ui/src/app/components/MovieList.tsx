"use client";
import { useEffect, useState } from 'react';
import { getApiUrl } from './api'
import 'bootstrap/dist/css/bootstrap.min.css';

interface Movie {
  id: number;
  title: string;
  releaseYear: number;
}

interface MovieListProps {
  env: {
      SERVER_URL?: string;
  };
}

export default function MovieList({ env }: MovieListProps) {
    const [movies, setMovies] = useState<any[]>([]);
    const [page, setPage] = useState<number>(1);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        require('bootstrap/dist/js/bootstrap.bundle.min.js');

        const fetchMovies = async () => {
            try {
                if (!env.SERVER_URL) {
                    throw new Error('API URL is not defined');
                }
                console.log(`Server URL: ${env.SERVER_URL}`);

                const res = await fetch(`${env.SERVER_URL}/api/movies?page=${page}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    cache: "no-store"
                });

                if (!res.ok) {
                    throw new Error("Failed to load movies");
                }

                const data = await res.json();
                setMovies(data.movieList); // Adjust this based on the API response structure
            } catch (error) {
                if (error instanceof Error) {
                    setError(error.message);
                    console.error('Fetch error:', error.message);
                } else {
                    console.error('An unknown error occurred:', error);
                }
            }
        };

        fetchMovies();
    }, [page, env.SERVER_URL]);

    const handleNextPage = () => setPage((prev) => prev + 1);
    const handlePrevPage = () => setPage((prev) => (prev > 1 ? prev - 1 : 1));

  return (
  <div className="container-fluid">
      <h1 className="text-center my-2">MOVIE LIST</h1>
        <div className="accordion" id="accordionExample">
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
    <div className="pagination-buttons mt-3 text-center">
        <button className="btn btn-primary" onClick={handlePrevPage} disabled={page === 1}>Previous</button>
        <span className="mx-1">Page {page}</span>
        <button className="btn btn-primary" onClick={handleNextPage}>Next</button>
    </div>
  </div>
  );
}
