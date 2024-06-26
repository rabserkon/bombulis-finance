


import {useEffect, useState} from "react";

import axios from "axios";
import {Avatar, Typography, theme, Dropdown, Menu} from "antd";
import {UserOutlined} from "@ant-design/icons";
import {Header} from "antd/es/layout/layout";
import Link from "next/link";

const { Text } = Typography;

function UserBar({ jwtToken }){

    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        async function fetchData() {
            try {
                const response = await axios.get('/api/user/v1/user/info', {
                    headers: {
                        'Authorization': jwtToken,
                        'Content-Type': 'application/json',
                    },
                    params: {

                    }
                });
                setUserData(response.data);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        }
        fetchData();
    }, [jwtToken]);

    const UserMenu = (
        <Menu>
            <Menu.Item key="settings">Настройки</Menu.Item>
            <Menu.Item key="logout" style={{ color: 'red' }}>
                <Link href={`/logout`}>Выйти</Link>
            </Menu.Item>
        </Menu>
    );

    const [collapsed, setCollapsed] = useState(false);
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();
    return(

            <Header
                style={{
                    padding: 0,
                    background: colorBgContainer,
                    display: 'flex',
                    alignItems: 'right',
                    justifyContent: 'flex-end',
                }}
            >

                    <div style={{ paddingRight: '16px' }}>
                        <Dropdown overlay={UserMenu} trigger={['click']}>
                            <div>
                                <Text style={{ marginRight: '8px', marginBottom: 0 }}>{loading ? ('Loading...') : (userData?.userInfo.login)}</Text>
                                <Avatar
                            size="large"
                            icon={loading ?  <UserOutlined /> : userData?.userInfo.cropAvatarPath}
                            src={loading ?  <UserOutlined /> : userData?.userInfo.cropAvatarPath}
                            style={{ marginRight: '16px' }}
                        />
                            </div>
                        </Dropdown>
                    </div>
            </Header>

    )
}

export default UserBar;