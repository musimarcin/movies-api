"use client";
import styles from "./page.module.css";
import { useState, useEffect } from "react";
import { Movie, EnvListProps, MovieListProps } from "@/app/components/Movies"
import MovieList from "@/app/components/MovieList"


export default function Search() {
const [movies, setMovies] = useState<any[]>([]);
const [query, setQuery] = useState<string>("");
const [error, setError] = useState<string | null>(null);

    const fetchMovies = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
          const res = await fetch(`/api?query=${query}`, {
            method: "GET",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
            },
            cache: "no-store",
          });
          const data = await res.json();
          console.log(data)

        if (!res.ok) throw new Error("Failed to load movies")

        setMovies(data.movieList.movieList);
        } catch (error) {
            if (error instanceof Error) {
                console.error("Fetch error:", error.message);
                setError(error.message);
            } else {
                console.error("An unknown error occurred:", error);
            }
        }
    };


    return (
        <div className="d-flex justify-content-center">
            <form onSubmit={fetchMovies} className={`${styles.add} mt-3 bg-light p-3`}>
                <div className="mb-3">
                    <label className="form-label">Movie title</label>
                    <input className="form-control"
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        required />
                </div>
                <button type="submit" className="btn btn-primary">Search</button>
                {movies.length > 0 && <MovieList movies={movies} />}
            </form>
        </div>
    );
}