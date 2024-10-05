"use client";
import { useState } from 'react';
import styles from "./page.module.css";
import MovieList from '@/app/components/MovieList';
import {getApiUrl} from '@/app/components/api'


export default function Search() {
    const [query, setQuery] = useState<string>("");
    const [movies, setMovies] = useState([]);

    const findMovie = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            const apiUrl = getApiUrl();
                const res = await fetch(`${apiUrl}/api/movies`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
                cache: "no-store"
            });

            if (!res.ok) {
                throw new Error('Failed to fetch movies');
            }

            const data = await res.json();
            setMovies(data.movieList);
        } catch (error) {
            console.error("Error fetching movies:", error);
        }
    };

    return (
        <div className="d-flex justify-content-center">
            <form onSubmit={findMovie} className={`${styles.add} mt-3 bg-light p-3`}>
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
