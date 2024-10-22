import { NextResponse } from "next/server";

export async function POST(request: Request) {
    const payload = await request.json();

    try {
        const apiResponse = await fetch(`${process.env.SERVER_URL}/api/auth/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (!apiResponse.ok) {
            return NextResponse.json({ error: "Failed to register" }, { status: 500 });
        }

        return NextResponse.json({ message: 'User registered' });

    } catch (error) {
        console.error("Error registering:", error);
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}