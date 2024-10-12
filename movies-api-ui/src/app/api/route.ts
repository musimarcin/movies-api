//naming route.ts is important for local fetching in page.tsx
import { NextResponse } from "next/server";

export async function GET(request: Request) {
    const { searchParams } = new URL(request.url);
    const page = searchParams.get("page") || "1";
    const query = searchParams.get("query") || "";
    try {
        let apiResponse;

        if (query === "") {
            apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies?page=${page}`);
        } else {
            apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies?query=${query}`);
        }

        if (!apiResponse.ok) {
            return NextResponse.json({ error: "Failed to fetch movies" }, { status: 500 });
        }

        const movies = await apiResponse.json();

        return NextResponse.json({
            movieList: movies,
            page,
        });
    } catch (error) {
        console.error("Error fetching movies:", error);
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}

export async function POST(request: Request) {
    const payload = await request.json();

    try {
        const apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (!apiResponse.ok) {
            return NextResponse.json({ error: "Failed to create movie" }, { status: 500 });
        }

        const movie = await apiResponse.json();
        return NextResponse.json(movie);
    } catch (error) {
        console.error("Error fetching movies:", error);
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}

export async function DELETE(request: Request) {
    const payload = await request.json();

    try {
        const apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (!apiResponse.ok) {
            return NextResponse.json({ error: "Failed to delete movie" }, { status: 500 });
        }

       return NextResponse.json({ message: 'Movie deleted successfully' });

    } catch (error) {
        console.error("Error deleting movie:", error);
        return NextResponse.json({ error: "Internal Server Error" }, { status: 500 });
    }
}