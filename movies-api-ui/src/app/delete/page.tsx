"use client";
import styles from "./page.module.css";
import { useState, useEffect } from "react";



export default function Delete() {
    const [title, setTitle] = useState<string>("");
    const [releaseYear, setReleaseYear] = useState<number | "">("");
    const [message, setMessage] = useState<string | null>(null);

    const deleteMovie = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const payload = {
           title,
           releaseYear,
        };

        try {
            const res = await fetch(`/api`, {
                method: "DELETE",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            const data = await res.json();
            if (!res.ok) {
                setMessage(data.error);
            }

            setMessage(data.message);
        } catch (error) {
            if (error instanceof Error) {
                setMessage(error.message);
            } else {
                setMessage("An unknown error occurred.");
            }
        }
        setTitle("");
        setReleaseYear("");
    }

    return (
        <div className="d-flex justify-content-center">
            <form onSubmit={deleteMovie} className={`${styles.add} mt-3 bg-light p-3`}>
              <div className="mb-3">
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
              </div>
              <button type="submit" className="btn btn-primary">Delete</button>
              {message && <div className="mt-3 alert alert-info">{message}</div>}
            </form>
        </div>
    );
}
