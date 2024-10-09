"use client";
import { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Movie, EnvListProps } from "./components/Movies"
import { MovieList } from "./components/MovieList"


export default function HomeMovie({ env }: EnvListProps) {
    const [movies, setMovies] = useState<any[]>([]);
    const [page, setPage] = useState<number>(1);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        require('bootstrap/dist/js/bootstrap.bundle.min.js');
        const fetchMovies = async () => {
            try {
                const res = await fetch(`${ env.SERVER_URL }/api/movies?page=${ page }`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    cache: "no-store"
                });
                const data = await res.json()

                if (!res.ok) {
                    throw new Error("Failed to load movies");
                }

                setMovies(data.movieList);
            } catch (error) {
                if (error instanceof Error) console.error('Fetch error:', error.message);
                else console.error('An unknown error occurred:', error);
            }
        };

        fetchMovies();
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
