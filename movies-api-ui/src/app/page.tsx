import Image from "next/image";
import styles from "./page.module.css";
import MovieList from './components/MovieList';
import { useState } from 'react';

export default async function Home() {
        const res = await fetch("http://localhost:18080/api/movies", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            cache: "no-store"
        });
        const data = await res.json();
        let movies = data.movieList;
        console.log(res)
        console.log(data)
        console.log(movies)
  return (
      <>
      <div className="container-fluid">
          <h1>Movie List</h1>
          <MovieList movies={movies} />
      </div>
    </>
  );
}
