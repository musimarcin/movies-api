"use client"
import { useState } from 'react';
import styles from "./page.module.css";
import {getApiUrl} from '@/app/components/api'

export default function Add() {
       const [title, setTitle] = useState<string>("");
       const [releaseYear, setReleaseYear] = useState<number | "">("");
       const [description, setDescription] = useState<string>("");
       const [message, setMessage] = useState<string | null>(null);

       const addMovie = async (e: React.FormEvent<HTMLFormElement>) => {
           e.preventDefault();

           const payload = {
               title,
               releaseYear,
           };

        try {
            const apiUrl = getApiUrl();
                const res = await fetch(`${apiUrl}/api/movies`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json" ,
                    },
                    body: JSON.stringify(payload),
                });

                if (!res.ok) {
                    throw new Error('Network response was not ok');
                }

                setMessage('Movie added successfully!');
            } catch (error) {
                setMessage('Failed to add movie. Please try again.');
            }
            setTitle("");
            setReleaseYear("");
            setDescription("");
       };

    return (
        <div className="d-flex justify-content-center">
            <form onSubmit={addMovie} className={`${styles.add} mt-3 bg-light p-3`}>
              <div className="d-flex mb-3">
                <div className="me-auto">
                <label className="form-label">Movie title</label>
                <input className="form-control" value={title} onChange={(e) => setTitle(e.target.value)} required/>
                </div>
                <div>
                <label className="form-label">Release Year</label>
                <input className="form-control" value={releaseYear} onChange={(e) => setReleaseYear(e.target.value ? parseInt(e.target.value) : "")}/>
                </div>
              </div>
              <div className="mb-3">
                <label className="form-label">Description</label>
                <input className="form-control" value={description} onChange={(e) => setDescription(e.target.value)}/>
              </div>
              <button type="submit" className="btn btn-primary">Add</button>
              {message && <div className="mt-3 alert alert-info">{message}</div>}
            </form>
        </div>
    );
}
