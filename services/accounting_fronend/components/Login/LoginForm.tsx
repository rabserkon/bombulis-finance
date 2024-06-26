import React, {useEffect, useState} from "react";
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
const { Title } = Typography;
import "./login.css"
import axios from "axios";
import { useRouter } from 'next/navigation'


function LoginForm(){
    const [loginGlobal, setLoginGlobal] = useState(null);
    const [authStatus, setAuthStatus] = useState('notFound');
    const [authMethod, setAuthMethod] = useState('byPassword');
    const [verificationCode, setVerificationCode] = useState('');
    const [jwt, setJWT] = useState(null);
    const options = [
        { label: 'Password', value: 'byPassword' },
        { label: 'Code', value: 'byCode' },
    ];
    const router = useRouter();
    const [form] = Form.useForm();
    const handleSegmentChange = (value) => {

        let byPassword = false;
        let byCode = false;
        switch (value) {
            case 'Password':
                byPassword = true;
                break;
            case 'Code':
                byCode = true;
                break;
            default:
                break;
        }
        form.setFieldsValue({ byPassword, byCode });
    };

    const handleChangeLoginMethod = (val) => {

    }

    const handleVerificationCodeChange = (e) => {

    };

    const resendAuthCode = () => {
        axios.post(`/api/auth/v1/validate/user`, null,{
            params: {
                principal: loginGlobal,
                method: 'byCode'
            }
        }).then(response => {
            notification.success({
                message: 'Уведомление!',
                description: "Код отправлен",
            });
        }).catch((error) => {
            console.error('Ошибка:', error.response.data);
            notification.error({
                message: 'Ошибка!',
                description: error.response.data.message,
            });
        });
    }

    const handleOk = (newVal) => {

    }

    useEffect(() => {
        if (authStatus === "success"){
            axios.post(`/auth/callback?jwt=${jwt}`)
                .then(response => {
                    if (response.status === 200) {
                        router.push('/accounting');
                    } else {
                        notification.error({
                            message: 'Ошибка!',
                            description: "Ошибка сервиса",
                        });
                    }
                })
                .catch(error => {
                    notification.error({
                        message: 'Ошибка!',
                        description: "Ошибка сервиса",
                    });
                });
        }
    },[authStatus])

    const onSend= async (values) => {
        if (authStatus === 'userExists'){
            values.method = authMethod;
            axios.post(`/api/auth/v1/login`, null,{
                params: {
                    principal: values.username,
                    method: values.method,
                    credentials: values.credentials
                }
            }).then(response => {
                if (response.data.status === true){
                    notification.success({
                        message: 'Успешно!',
                        description: response.data.message,
                    });
                    setAuthStatus("success")
                    setJWT(response.data.jwt)
                } else {
                    notification.error({
                        message: 'Ошибка!',
                        description: response.data.message,
                    });
                }
            }).catch((error) => {
                console.error('Ошибка:', error.response.data);
                notification.error({
                    message: 'Ошибка!',
                    description: error.response.data.message,
                });
            })
        } else {
            values.method = values.method || options[0].value;
            form
                .validateFields()
                .then((val) => {
                    axios.post(`/api/auth/v1/validate/user`, null,{
                        params: {
                            principal: values.username,
                            method: values.method
                        }
                    }).then(response => {
                        setAuthStatus(response.data.status)
                        setLoginGlobal(response.data.principalGlobal)
                        setAuthMethod(response.data.method)
                    }).catch((error) => {
                        console.error('Ошибка:', error.response.data);
                        notification.error({
                            message: 'Ошибка!',
                            description: error.response.data.message,
                        });
                    });
                }).catch(error => {
                console.log("Error:", error.response.data)
            });
        }

    };

    return(
        <div className={"login-form"} >
            <Title level={1} style={{paddingBottom: '15px', textAlign: 'center'}}>Войти</Title>
            <Card style={{paddingBottom:'10px',paddingTop: '30px'}}>
                <Form
                    name="normal_login"
                    initialValues={{ remember: true }}
                    style={{ width: 300 }}
                    onFinish={onSend}
                >
                    <Form.Item
                        name="username"
                        rules={[{ required: true, message: 'Пожалуйста введите имя или email!' }]}>
                        <Input disabled={authStatus === 'userExists'} prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Username or email..." />
                    </Form.Item>
                    {
                        authStatus === 'userExists' ? (
                                <Form.Item
                                    name="credentials"
                                    rules={[{ required: true, message: 'Пожалуйста введите код или пароль.' }]}>
                                    {authMethod === 'byPassword' ? (
                                        <Input.Password placeholder="Password" />
                                    ) : (
                                        <Input placeholder="Verification code" />
                                    )}

                                </Form.Item>
                        ) : null
                    }
                    <Form.Item
                        name="method"
                        style={{ width: '100%' }}>
                            <Radio.Group
                                disabled={authStatus === 'userExists'}
                                optionType="button"
                                defaultValue={options[0].value}
                                onChange={handleChangeLoginMethod}
                                style={{ width: '100%' }}
                            >
                                {options.map((option, index) => (
                                    <Radio.Button style={{width: '50%', alignItems: 'center' }} key={option.value} value={option.value}>
                                        {option.label}
                                    </Radio.Button>
                                ))}
                            </Radio.Group>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
                            Log in
                        </Button>
                    </Form.Item>
                </Form>
                {
                    authStatus === 'userExists' && (
                        authMethod === 'byCode' ? (
                            <Button onClick={resendAuthCode} optionType="button" htmlType="submit" style={{ width: '100%' }}>
                                Отправить код еще раз
                            </Button>
                        ) : (null)
                    )
                }
            </Card>
        </div>
    )
}

export default LoginForm;