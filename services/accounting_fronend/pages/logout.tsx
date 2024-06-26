import Cookies from 'cookies'
import { useRouter } from 'next/router';
import {NextResponse} from "next/server";

const Logout = () => null;


export async function getServerSideProps ({ req, res}) {
    let statusLogout = false;
    const cookies = new Cookies(req, res);
    if (cookies.get('jwtToken')) {
        const response = await fetch(process.env.auth_url_module + '/logout', {
            method: 'GET',
            headers: {
                Authorization: cookies.get('jwtToken')
            }
        });
        if (response.status === 200){
            statusLogout  = true
        }
        cookies.set('jwtToken');
    }
    return {
        redirect: {
            destination: '/login',
            permanent: false,
            query: {
                statusLogout: statusLogout,
            },
        }
    }
}

export default Logout;