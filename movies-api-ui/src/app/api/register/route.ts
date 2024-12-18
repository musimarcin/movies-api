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

        const data = await apiResponse.text() || "An unknown error occurred.";
        if (!apiResponse.ok) {
            return NextResponse.json({ error: data }, { status: apiResponse.status });
        }

        return NextResponse.json({ message: data });
    } catch (error) {
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}