import { NextResponse } from "next/server";

export async function POST(request: Request) {
    const payload = await request.json();

    try {
        const apiResponse = await fetch(`${process.env.SERVER_URL}/api/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (!apiResponse.ok) {
            return NextResponse.json({ error: "Failed to login movie" }, { status: 500 });
        }

        const movie = await apiResponse.json();
        return NextResponse.json(movie);
    } catch (error) {
        console.error("Error fetching movies:", error);
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}