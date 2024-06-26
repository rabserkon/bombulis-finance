import Layout from "antd/es/layout/layout";
import UserBar from "../components/accouting/User/UserBar";
import FinanceSpace from "../components/accouting/FinanceSpace";
import React from "react";
import {
    Button,
    Form,
    List,
    Select,
    Space,
    TreeSelect,
    Input,
    Col,
    Row,
    notification,
    Switch,
    InputNumber,
    Segmented,
    Typography,
    Radio, Card
} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import LoginForm from "../components/Login/LoginForm";
const { Title } = Typography;
import "../components/Login/login.css"
import Cookies from 'cookies'
import {NextResponse} from "next/server";
import axios from "axios";

function LoginPage() {

    return (
        <div style={{ display: 'flex', justifyContent: 'center',  height: '90vh' }}>
            <LoginForm/>
        </div>

    );
}

export async function getServerSideProps ({ req, res}) {
    try {
        const cookies = new Cookies(req, res);
        const jwtToken = cookies.get('jwtToken');
        if (jwtToken) {
            const response = await fetch(process.env.auth_url_module + '/auth/status', {
                method: 'GET',
                headers: {
                    Authorization: jwtToken
                }
            });
            if (response.status === 200) {
                return {
                    redirect: {
                        destination: '/accounting',
                        permanent: false,
                    },
                };
            }
        }
        return {
            props: {},
        };
    } catch (error) {
        return {
            props: {},
        };
    }
}


export default LoginPage;