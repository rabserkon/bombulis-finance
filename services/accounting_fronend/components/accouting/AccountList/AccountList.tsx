import Card from "antd/es/card/Card";
import {Button, Form, List, Select, Space, TreeSelect, Input, Col, Row, notification} from "antd";
import {EditOutlined, SwapRightOutlined} from "@ant-design/icons";
import {useEffect, useState} from "react";
import axios from "axios";
import Text from "antd/es/typography/Text";
import CreateAccountModal from './CreateAccountModal'
import {useDispatch, useSelector} from "react-redux";

const changeAccountType = [
    {
        title: 'archive',
        value: '0-0',
        key: '0-0',

    },]

function AccountList({jwtToken}){
    const dispatch = useDispatch();
    const [createAccountModalVisible, setCreateAccountModalVisible] = useState(false);
    const [initialValues, setInitialValues] = useState(null);
    const [currencyList, setCurrencyList] = useState([]);
    const accounts = useSelector(state => state.accounts);
    const [visibleAccountItems, setVisibleAccountItems] = useState(6);
    const [loading, setLoading] = useState(false);
    const [value, setValue] = useState();
    const [createAccountForm] = Form.useForm();
    const showMoreButtonVisible = accounts.length > visibleAccountItems;

    const onChange = (newValue) => {
        setValue(newValue);
    };

    const createAccountShowModal = (newValue) => {
        setCreateAccountModalVisible(true);
    };

    const getAccountInformation = (accountId) => {
        setInitialValues(null)
        axios.get(`/api/finance/v1/accounts/information`,{
            headers: {
                'Content-Type': 'application/json',
                'Authorization': jwtToken,
            },
            params: {
                accountId: accountId
            }
        })
            .then(response => {
                setInitialValues(response.data.account)
                setCreateAccountModalVisible(true);
            })
            .catch(error => {
                notification.error({
                    message: 'Ошибка' ,
                    description: error.response.data
                });
                console.error('Ошибка получения информации об аккаунте:', error);
            });
    };


    const handleShowMore = () => {
        setVisibleAccountItems(prev => prev + 6); // Показываем еще 6 элементов
    };

    const onPopupScroll = (e) => {
        console.log('onPopupScroll', e);
    };



    useEffect(() => {
        // Загружаем список валют с сервера
        axios.get('/api/finance/v1/currencies/all', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': jwtToken,
            }
        })
            .then(response => {
                setCurrencyList(response.data.currencies);
            })
            .catch(error => {
                notification.error({
                    message: 'Ошибка!',
                    description: 'Ошибка загрузки валют',
                });
                console.error('Ошибка загрузки списка валют:', error);
            });

        if (!currencyList) {
            notification.error({
                message: 'Ошибка!',
                description: 'Ошибка загрузки валют',
            });
        }
    }, []);

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await axios.get('/api/finance/v1/accounts/list', {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': jwtToken,
                    },
                    params: {

                    }
                });
                handleUpdateAccounts(response.data.accounts);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        }

        fetchData();
    }, [jwtToken]);

    const handleUpdateAccounts = (accountsList) => {
        const action = {
            type: 'SET_ACCOUNTS',
            payload: accountsList,
        };
        dispatch(action);
    };


    return (

        <Card title="Счета" className={'custom-card'} bordered={false} style={{
            width: '45%'
        }}>
            <div style={{
                width: '100%',
                display: 'flex',
                flexWrap: 'nowrap',
                marginBottom: '10px'
            }}>
                <TreeSelect
                    showSearch
                    style={{
                        width: '100%',
                        marginRight: '15px'
                    }}

                    treeData={changeAccountType}
                    showCheckedStrategy={"SHOW_ALL"}
                    dropdownStyle={{
                        maxHeight: 400,
                        overflow: 'auto',
                    }}
                    placeholder="Please select"
                    allowClear
                    treeDefaultExpandAll
                    onChange={onChange}
                    onPopupScroll={onPopupScroll}
                    treeCheckable={true}
                />
                <Button type="primary" onClick={createAccountShowModal}  style={{
                    maxWidth: '120px',
                    width: '35%',
                }}>Create</Button>
            </div>
            {loading ? (
                <Text style={{ marginRight: '8px', marginBottom: 0 }}>Loading...</Text>
            ) : (
                <List
                itemLayout="horizontal"

                dataSource={accounts.slice(0, visibleAccountItems)}
                renderItem={item => (
                    <List.Item  style={{
                        marginLeft: '5px',
                        width: '97%'
                    }}>
                        <div style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            width: '100%'
                        }}>
                            <div>{item.name}</div>
                            <div style={{
                                display: "flex",
                                justifyContent: "flex-end",
                                width: '30%'
                            }}>
                                <div>{item.balance.toFixed(2)} {item.currency.isoCode}</div>
                                <Button key={item.id} value={item.id} type="link" size={"small"} onClick={() => getAccountInformation(item.id)}>
                                    <EditOutlined style={{ fontSize: '16px',  color: '#808080'}} />
                                </Button>
                            </div>
                        </div>
                    </List.Item>

                )}
                loadMore={
                    showMoreButtonVisible && (
                        <div style={{ textAlign: 'center', marginTop: 12 }}>
                            <Button onClick={handleShowMore}>Показать еще</Button>
                        </div>
                    )
                }
            />)}

            <CreateAccountModal
                visible={createAccountModalVisible}
                jwtToken={jwtToken}
                currencyList={currencyList}
                setVisible={setCreateAccountModalVisible}
                values={initialValues}
                setValues={setInitialValues}
            />
        </Card>
    );

};

export default AccountList;