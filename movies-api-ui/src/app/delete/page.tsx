"use client";
import styles from "./page.module.css";
import { useState, useEffect } from "react";



export default function Delete() {
    const [title, setTitle] = useState<string>("");
    const [message, setMessage] = useState<string | null>(null);

    const deleteMovie = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const payload = {
           title
        };

        try {
            const res = await fetch(`/api`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
            });

            if (!res.ok) {
                throw new Error("Failed to delete movie");
            }

            setMessage("Movie deleted successfully!");
        } catch (error) {
            setMessage("Failed to delete movie. Please try again.");
        }
        setTitle("");
    }

    return (
        <div className="d-flex justify-content-center">
            <form onSubmit={deleteMovie} className={`${styles.add} mt-3 bg-light p-3`}>
              <div className="mb-3">
                <label className="form-label">Movie title</label>
                <input className="form-control" value={title} onChange={(e) => setTitle(e.target.value)} required/>
              </div>
              <button type="submit" className="btn btn-primary">Delete</button>
              {message && <div className="mt-3 alert alert-info">{message}</div>}
            </form>
        </div>
    );
}
