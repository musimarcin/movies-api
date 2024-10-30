"use client";
import { useState, useEffect } from "react";
import { Movie, EnvListProps, MovieListProps } from "./components/Movies"
import MovieList from "./components/MovieList"


export default function Home() {
  const [movies, setMovies] = useState<any[]>([]);
  const [page, setPage] = useState<number>(1);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // fetching movies with route.ts in api/
    const fetchMovies = async () => {
        try {
            const res = await fetch(`/api?page=${page}`, {
                method: "GET",
                credentials: "include",
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
                setError(error.message);
            } else {
                console.error("An unknown error occurred:", error);
            }
        }
    };

    useEffect(() => {
        async function fetchLoginStatus() {
              const res = await fetch('/api/getcookie');
              const data = await res.json();
              setIsLoggedIn(data.isLoggedIn);
        };
        fetchLoginStatus();
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
        {isLoggedIn ? (
            <>
              <h1 className="text-center my-2">MOVIE LIST</h1>
                <MovieList movies={movies}/>
            </>
        ) : null}
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
