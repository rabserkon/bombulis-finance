import Cookies from 'cookies'
import { useRouter } from 'next/router';
import {NextResponse} from "next/server";

const jsonReq = () => null;


export async function getServerSideProps ({ req, res}) {
    res.setHeader("Content-Type", "application/json");
    const {jwt} = req[Symbol.for('NextInternalRequestMeta')].initQuery;
    if (req.method !== 'POST' || !jwt || jwt === 'null') {
        return {
            notFound: true,
        };
    }

    if (jwt) {
        const cookies = new Cookies(req, res);
        cookies.set('jwtToken', jwt, {
            httpOnly: true,
        });
        res.write('{\"status\": "' + true + '" }');
        res.end()
        return {
            props:{

            }
        };
    }
    return {
        notFound: true,
    };
}

export default jsonReq;