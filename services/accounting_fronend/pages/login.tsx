import LoginForm from "../components/Login/LoginForm";
import "../components/Login/login.css"

function LoginPage() {

    return (
        <div style={{ display: 'flex', justifyContent: 'center',  height: '90vh' }}>
            <LoginForm/>
        </div>

    );
}

export async function getServerSideProps ({ req, res}) {
    return {
        props: {},
    };
}


export default LoginPage;