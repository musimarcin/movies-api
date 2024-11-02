"use client";
import styles from "./page.module.css";
import { useState, useEffect } from "react";
import { useRouter } from 'next/navigation';

export default function Login() {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [email, setEmail] = useState<string>("");
    const [message, setMessage] = useState<string | null>(null);
    const router = useRouter();

    const loginUser = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const payload = {
           username,
           password
        };

        try {
            const res = await fetch(`/api/login`, {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            const data = await res.json();
            if (!res.ok) {
                throw new Error(data.error || "Failed to login. Please try again.");
            }

            router.push('/');
        } catch (error) {
            if (error instanceof Error) {
                setMessage(error.message);
            } else {
                setMessage("An unknown error occurred.");
            }
        }
    };

    return (
        <div className="d-flex justify-content-center">
            <form onSubmit={loginUser} className={`${styles.login} mt-3 bg-light p-3`}>
              <div className="d-flex mb-3">
                <div className="me-auto">
                <label className="form-label">Username</label>
                <input className="form-control" value={username} onChange={(e) => setUsername(e.target.value)} required/>
                </div>
                <div>
                <label className="form-label">Password</label>
                <input className="form-control" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required/>
                </div>
              </div>
              <button type="submit" className="btn btn-primary">Login</button>
              {message && <div className="mt-3 alert alert-info">{message}</div>}
            </form>
        </div>
    );
}


