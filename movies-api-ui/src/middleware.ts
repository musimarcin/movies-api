import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {

    const token = request.cookies.get('token')?.value;

    const protectedPaths = ['/add', '/delete', '/search', '/logout'];
    const pathIsProtected = protectedPaths.some((path) => request.nextUrl.pathname.startsWith(path));

    if (pathIsProtected && !token) {
        return NextResponse.redirect(new URL('/login', request.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: ['/add', '/delete', '/search', '/'],
};
