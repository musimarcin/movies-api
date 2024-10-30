import { NextRequest, NextResponse } from 'next/server';
import { parse } from 'cookie';

export async function GET(request: NextRequest) {
    const token = request.cookies.get('token')?.value;
    return NextResponse.json({ isLoggedIn: Boolean(token) });
}
