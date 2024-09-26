import Image from "next/image";
import styles from "./page.module.css";
import 'bootstrap/dist/css/bootstrap.min.css';

interface Movie {
  id: number;
  title: string;
  releaseYear: number;
}

export default async function Home() {
    const res = await fetch('http://localhost:18080/api/movies', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    });

  const data = await res.json();
  let movies = data.movieList;

  return (
    <div>
      <h1>Movie List</h1>
      <ul>
        {movies.map((movie) => (
          <li key={movie.id}>{movie.title} {movie.releaseYear}</li>
        ))}
      </ul>
    </div>
  );
}
