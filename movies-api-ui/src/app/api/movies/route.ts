//naming route.ts is important for local fetching in page.tsx
import { NextResponse } from 'next/server';

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const page = searchParams.get('page') || '1';

  const apiResponse = await fetch(`${process.env.SERVER_URL}/api/movies?page=${page}`);
  if (!apiResponse.ok) {
    return NextResponse.json({ error: 'Failed to fetch movies' }, { status: 500 });
  }

  const movies = await apiResponse.json();

  return NextResponse.json({
    movieList: movies,
    page,
  });
}
