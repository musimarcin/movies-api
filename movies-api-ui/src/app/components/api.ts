import axios, {AxiosResponse} from "axios";
import {MovieResponse} from "./Movies";

export const dynamic = 'force-dynamic'

export const fetchMovies = async (page: number) => {
//     try {
        const res = await fetch(`${ process.env.SERVER_URL }/api/movies?page=${ page }`, {
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
        return data.movieList
//
//     } catch (error) {
//         if (error instanceof Error) console.error('Fetch error:', error.message);
//         else console.error('An unknown error occurred:', error);
//     }
};
// export const getApiUrl = async () => {
//
//     let apiUrl = process.env.NEXT_PUBLIC_SERVER_URL
//     console.log("server: " + process.env.NEXT_PUBLIC_SERVER_URL)
//     console.log("client: " + process.env.NEXT_PUBLIC_CLIENT_URL)
//     if (!apiUrl) {
//         console.error('API URL is not defined');
//         throw new Error('API URL is not defined');
//     }
//
//     return apiUrl;
// }
//
// export const fetchMoviesApi = async (page: number) => {
//     const apiUrl = getApiUrl();
//     const res = await axios.get(`${apiUrl}/api/movies?page=${page}`);
//     return res.data
// }