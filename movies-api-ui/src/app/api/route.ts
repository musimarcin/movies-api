//naming route.ts is important for local fetching in page.tsx
import { NextResponse } from "next/server";
import { cookies } from 'next/headers'

export async function GET(request: Request) {
    const { searchParams } = new URL(request.url);
    const page = searchParams.get("page") || "1";
    const query = searchParams.get("query") || "";
    const cookieStore = await cookies();
    const token = cookieStore.get("token") || "";

    if (token == "") return NextResponse.json({ error: "Not logged in" }, { status: 401 });

    try {
        let apiResponse;

        if (query === "") {
            apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies?page=${page}`, {
                method: "GET",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                    "Cookie": token ? `token=${token.value}` : '',
                },
            });
        } else {
            apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies?query=${query}`, {
                method: "GET",
                credentials: "include",
                headers: {
                   "Content-Type": "application/json",
                   "Cookie": token ? `token=${token.value}` : '',
                },
            });
        }

        if (!apiResponse.ok) {
            return NextResponse.json({ error : "Movie not found." }, { status : apiResponse.status });
        }

        const movies = await apiResponse.json();
        const res = NextResponse.json({
            movieList: movies,
            page,
        });
        return res;

    } catch (error) {
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}

export async function POST(request: Request) {
    const payload = await request.json();
    const cookieStore = await cookies();
    const token = cookieStore.get('token');
    try {
        const apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
                "Cookie": token ? `token=${token.value}` : '',
            },
            body: JSON.stringify(payload),
        });

        if (!apiResponse.ok) {
            return NextResponse.json({ error: "Failed to create movie" }, { status: 500 });
        }

        const movie = await apiResponse.json();
        const res = NextResponse.json(movie);
        return res;

    } catch (error) {
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}

export async function DELETE(request: Request) {
    const payload = await request.json();
    const cookieStore = await cookies();
    const token = cookieStore.get('token');

    try {
        const apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies`, {
            method: "DELETE",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
                "Cookie": token ? `token=${token.value}` : '',
            },
            body: JSON.stringify(payload),
        });

        const data = await apiResponse.text();
        if (!apiResponse.ok) {
            return NextResponse.json({ error: data }, { status: apiResponse.status });
        }

       return NextResponse.json({ message: data });

    } catch (error) {
        return NextResponse.json({ error }, { status: 500 });
    }
}