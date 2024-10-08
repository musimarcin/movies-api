import MovieList from './components/MovieList';

export const dynamic = "force-dynamic";

export default function Home() {

    return (
        <>
        <MovieList env={{ SERVER_URL: process.env.SERVER_URL }} />
        </>
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