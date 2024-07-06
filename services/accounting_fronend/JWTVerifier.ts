import * as jose from 'jose'

const secret = new Uint8Array(Buffer.from('85f7dd24c7e24c44c90988742465dda35c75f40286ed4fa566a3a8fd5c0daefe', 'base64'));

class JWTVerifier {

    async verify(token) {
        try {
            const { payload: jwtData } = await jose.jwtVerify(
                token, secret, {
                    algorithms: ["HS256"],
                }
            );
            return jwtData
        } catch (err) {
            console.log(err)
            throw 'InvalidToken';
        }
    }
}

export default JWTVerifier;