import {NextFetchEvent, NextResponse} from 'next/server'
import type { NextRequest } from 'next/server'
import JWTVerifier from "./JWTVerifier";

export async function middleware(request: NextRequest, event: NextFetchEvent) {
    try {
        if (
            new URL(request.url).pathname.startsWith('/api/auth') ||
            new URL(request.url).pathname === '/login'
        ) {
            return NextResponse.next();
        }

        if (!request.cookies.has("jwtToken")) {
           throw 'tokenIsNull'
        }

        let token = request.cookies.get('jwtToken').value
        const verifier = new JWTVerifier();
        const decodedToken = await verifier.verify(token);
        const requestHeaders = new Headers(request.headers);
        requestHeaders.set('Authorization', `Bearer ${token}`);
        return NextResponse.next({ request: { headers: requestHeaders } });
    } catch (e){
        const redirectUrl = new URL('/login', request.url);
        redirectUrl.searchParams.set('error', e);
        return NextResponse.redirect(redirectUrl);
    }
}

export const config = {
    matcher: ['/accounting/:path*', '/dashboard/:path*', '/accounting', '/api/:path*', '/logout'],
}