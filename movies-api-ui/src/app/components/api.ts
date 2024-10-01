import axios, {AxiosResponse} from "axios";
import {MovieResponse} from "./Movies";

const API_URL = "http://localhost:8080";

export const fetchMoviesApi = async (page: number) => {
    const res = await axios.get(`${API_URL}/api/movies?page=${page}`);
    return res.data;
}