"use client";
import { useState } from 'react';
import styles from "./page.module.css";

export default function Delete() {
    const [title, setTitle] = useState<string>("");  // Movie title for deletion
    const [message, setMessage] = useState<string | null>(null);  // Success/Error message

    // Function to delete a movie
    const deleteMovie = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:18080/api/movies", {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ title }),  // Send the title to delete
            });

            if (!response.ok) {
                throw new Error('Failed to delete movie');
            }

            setMessage('Movie deleted successfully!');
            setTitle("");  // Clear input after successful deletion
        } catch (error) {
            setMessage('Failed to delete movie. Please try again.');
        }
    };

    return (
        <div className="d-flex justify-content-center">
            <form onSubmit={deleteMovie} className={`${styles.add} mt-3 bg-light p-3`}>
              <div className="mb-3">
                <label className="form-label">Movie title</label>
                <input
                    className="form-control"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                />
              </div>
              <button type="submit" className="btn btn-primary">Submit</button>
              {message && <div className="mt-3 alert alert-info">{message}</div>}
            </form>
        </div>
    );
}
