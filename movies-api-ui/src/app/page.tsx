"use client";
import { useState, useEffect } from "react";
import { Movie, EnvListProps, MovieListProps } from "./components/Movies"
import MovieList from "./components/MovieList"
//import "bootstrap/dist/css/bootstrap.min.css";

//export const dynamic = "force-dynamic";

export default function Home() {
  const [movies, setMovies] = useState<any[]>([]);
  const [page, setPage] = useState<number>(1);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState<number>(1);

  // fetching movies with route.ts in api/movies/
    const fetchMovies = async () => {
        try {
            const res = await fetch(`/api?page=${page}`, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
            cache: "no-store",
            });
            const data = await res.json();

            if (!res.ok) throw new Error("Failed to load movies")

            setMovies(data.movieList.movieList);
            setTotalPages(data.movieList.totalPages);

        } catch (error) {
            if (error instanceof Error) {
                console.error("Fetch error:", error.message);
                setError(error.message);
            } else {
                console.error("An unknown error occurred:", error);
            }
        }
    };

  useEffect(() => {
    fetchMovies();
  }, [page]);

  const handleNextPage = () => {
    if (page < totalPages) setPage(page + 1);
  };

  const handlePrevPage = () => {
    if (page > 1) setPage(page - 1);
  };

  return (
    <>
      <div className="container-fluid">
          <h1 className="text-center my-2">MOVIE LIST</h1>
            <MovieList movies={movies}/>
      <div className="pagination-buttons mt-3 text-center">
        <button className="btn btn-primary" onClick={handlePrevPage} disabled={page === 1}>
          Previous
        </button>
        <span className="mx-1">Page {page} of {totalPages}</span>
        <button className="btn btn-primary" onClick={handleNextPage} disabled={page === totalPages}>
          Next
        </button>
      </div>
    </div>
    </>
  );
}
