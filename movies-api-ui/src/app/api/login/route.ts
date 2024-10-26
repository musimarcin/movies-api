import { NextResponse } from "next/server";

export async function POST(request: Request) {
    const payload = await request.json();

    try {
        const apiResponse = await fetch(`${process.env.SERVER_URL}/api/auth/login`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload),
        });

        if (!apiResponse.ok) {
            return NextResponse.json({ error: "Failed to login user" }, { status: 500 });
        }

        const tokenCookie = apiResponse.headers.get('set-cookie');
        console.log(tokenCookie)
        const jwt = await apiResponse.json();
        const res = NextResponse.json(jwt);
        if (tokenCookie) res.headers.set('set-cookie', tokenCookie);
        return res;
    } catch (error) {
        console.error("Error fetching movies:", error);
        return NextResponse.json({ error: "Internal Server Errorand" }, { status: 500 });
    }
}