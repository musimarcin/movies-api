import axios, {AxiosResponse} from "axios";
import {MovieResponse} from "./Movies";


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