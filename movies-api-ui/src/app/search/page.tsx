"use client";
import styles from "./page.module.css";
import { useState, useEffect } from "react";
import { Movie, EnvListProps, MovieListProps } from "@/app/components/Movies"
import MovieList from "@/app/components/MovieList"


export default function Search() {
    const [movies, setMovies] = useState<any[]>([]);
    const [query, setQuery] = useState<string>("");
    const [message, setMessage] = useState<string | null>(null);

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
        if (!res.ok) throw new Error(data.error || "Failed to find a movie.")

        setMovies(data.movieList.movieList);
        } catch (error) {
            if (error instanceof Error) {
                setMessage(error.message);
            } else {
                setMessage("An unknown error occurred.");
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
                {message && <div className="mt-3 alert alert-info">{message}</div>}
                {movies.length > 0 && <MovieList movies={movies} />}
            </form>
        </div>
    );
}