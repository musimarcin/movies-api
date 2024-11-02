"use client";
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

export default function Logout() {
    const router = useRouter();
    const [message, setMessage] = useState<string | null>(null);

    const logout = async () => {
        try {
            const res = await fetch(`/api/logout`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json" ,
                },
            });
            const data = await res.json();

            if (!res.ok) {
                throw new Error(data.error || "Failed to register user. Please try again.");
            }
            setMessage(data.message);
        } catch (error) {
            if (error instanceof Error) {
                setMessage(error.message);
            } else {
                setMessage("An unknown error occurred.");
            }
        }
    }

    useEffect(() => {
        const handleLogout = async () => {
            await logout();
            router.push('/login');
        };

        handleLogout();
    }, [message, router]);

    return null;
}