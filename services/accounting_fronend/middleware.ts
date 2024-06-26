import {NextFetchEvent, NextResponse} from 'next/server'
import type { NextRequest } from 'next/server'

export async function middleware(request: NextRequest, event: NextFetchEvent) {
    try {
        if (new URL(request.url).pathname.startsWith('/api/auth')) {
            return NextResponse.next();
        }
        if (!request.cookies.has("jwtToken")) {
           throw 'tokenIsNull'
        }
        let jwtToken = request.cookies.get('jwtToken').value
        const requestHeaders = new Headers(request.headers)
        const responseAuth = await fetch( process.env.auth_url_module + '/auth/status', {
            method: 'GET',
            headers: {
                Authorization: jwtToken
            }
        }).catch(error =>{
            throw 'serviceNotWorks'
        });
        const response = NextResponse.next({request:{headers: requestHeaders}})
        response.headers.set('Authorization', jwtToken)
        if (responseAuth.status === 200){
            return response
        }
        throw 'notValidToken'
    } catch (e){
        const redirectUrl = new URL('/login', request.url);
        redirectUrl.searchParams.set('error', e);
        return NextResponse.redirect(redirectUrl);
    }
}

export const config = {
    matcher: ['/accounting/:path*', '/dashboard/:path*', '/accounting', '/api/:path*', '/logout'],
}