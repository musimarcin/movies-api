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

        const data = await apiResponse.text() || "An unknown error occurred.";
        if (!apiResponse.ok) {
            return NextResponse.json({ error: data }, { status: apiResponse.status });
        }

        const tokenCookie = apiResponse.headers.get('set-cookie');
        const res = NextResponse.json({ message: data });
        if (tokenCookie) res.headers.set('set-cookie', tokenCookie);
        return res;
    } catch (error) {
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}