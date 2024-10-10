"use client";
import { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Movie, EnvListProps, MovieListProps } from "./components/Movies"
import MovieList from "./components/MovieList"
import {fetchMovies} from "./components/api"

export default function HomeMovie({ movies }: MovieListProps) {
    const [page, setPage] = useState<number>(1);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        require('bootstrap/dist/js/bootstrap.bundle.min.js');
    }, [page]);

    const handleNextPage = () => setPage((prev) => prev + 1);
    const handlePrevPage = () => setPage((prev) => (prev > 1 ? prev - 1 : 1));

  return (
  <div className="container-fluid">
      <h1 className="text-center my-2">MOVIE LIST</h1>
        <MovieList movies={movies} />
    <div className="pagination-buttons mt-3 text-center">
        <button className="btn btn-primary" onClick={handlePrevPage} disabled={page === 1}>Previous</button>
        <span className="mx-1">Page {page}</span>
        <button className="btn btn-primary" onClick={handleNextPage}>Next</button>
    </div>
  </div>
  );
}
