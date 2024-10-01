"use client";

import { useState, useEffect } from "react";
import MovieList from './components/MovieList';

export default function Home() {
    const [movies, setMovies] = useState<any[]>([]);
    const [page, setPage] = useState<number>(1);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const res = await fetch(`http://localhost:8080/api/movies?page=${page}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    cache: "no-store",
                });
                const data = await res.json();

                if (!res.ok) {
                    throw new Error("Failed to load movies");
                }

                setMovies(data.movieList);
            } catch (error) {
                setError(error.message);
                console.log(error);
            }
        };

        fetchMovies();
    }, [page]);

    const handleNextPage = () => setPage((prev) => prev + 1);
    const handlePrevPage = () => setPage((prev) => (prev > 1 ? prev - 1 : 1));

    return (
        <div className="container-fluid">
            <h1 className="text-center my-2">MOVIE LIST</h1>
            {error ? <p>Error: {error}</p> : <MovieList movies={movies} />}

            <div className="pagination-buttons mt-3 text-center">
                <button className="btn btn-primary" onClick={handlePrevPage} disabled={page === 1}>Previous</button>
                <span className="mx-1">Page {page}</span>
                <button className="btn btn-primary" onClick={handleNextPage}>Next</button>
            </div>
        </div>
    );
}

//SERVER SIDE VERSION NOT WORKING LOCALLY: ENCONNREFUSED
/*
import MovieList from './components/MovieList';
import Link from 'next/link';

export default async function Home({ searchParams }) {
    const page = searchParams.page ? parseInt(searchParams.page) : 1;

    const res = await fetch(`http://localhost:8080/api/movies?page=${page}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        cache: "no-store"
    });

    if (!res.ok) {
        throw new Error('Failed to load movies');
    }

    const data = await res.json();
    const movies = data.movieList;
    const totalPages = data.totalPages || 1;

    return (
        <>
            <div className="container-fluid">
                <h1>Movie List</h1>
                <MovieList movies={movies} />

                <div className="pagination-controls">
                    {page > 1 && (
                        <Link href={`/?page=${page - 1}`}>
                            <a className="btn btn-primary">Previous</a>
                        </Link>
                    )}
                    <span className="page-number">Page {page} of {totalPages}</span>
                    {page < totalPages && (
                        <Link href={`/?page=${page + 1}`}>
                            <a className="btn btn-primary">Next</a>
                        </Link>
                    )}
                </div>
            </div>
        </>
    );
}
*/