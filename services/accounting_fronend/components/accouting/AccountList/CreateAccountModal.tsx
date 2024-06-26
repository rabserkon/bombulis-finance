import {Button, Form, List, Select, Space, TreeSelect, Input, Col, Row, notification, Switch, InputNumber} from "antd";
import Modal from "antd/es/modal/Modal";
import axios from "axios";
import {useEffect, useState} from "react";
import DeleteConfirmationModal from "./DeleteAccountConfirmationModal";
import {useDispatch, useSelector} from "react-redux";

const CreateAccountModal = ({visible: initialVisible, values: initialValues, currencyList, jwtToken, updateAccountList, setVisible, setValues}) => {
    const dispatch = useDispatch();
    const [createAccountForm] = Form.useForm();
    const [defaultCurrencyValue, setDefaultCurrencyValue] = useState(null);
    const [accountTypeList, setAccountTypeList] = useState([]);
    const [selectedAccountType, setSelectedAccountType] = useState(null);
    const [selectedAccountCurrency, setSelectedAccountCurrency] = useState(null);
    const [deleteConfirmationVisible, setDeleteConfirmationVisible] = useState(false);

    const accounts = useSelector(state => state.accounts);

    const handleUpdateAccounts = (accountsList) => {

        const action = {
            type: 'SET_ACCOUNTS',
            payload: accountsList,
        };
        dispatch(action);
    };


    const handleAccountTypeChange = (value) => {
        setSelectedAccountType(value);
    };

    const disabledEdit = initialValues !== null;

    useEffect(() => {
        axios.get('/api/finance/v1/accounts/types', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': jwtToken,
            }
        })
            .then(response => {
                setAccountTypeList(response.data);
            })
            .catch(error => {
                notification.error({
                    message: 'Ошибка!',
                    description: 'Ошибка загрузки типов',
                });
                console.error('Ошибка загрузки списка валют:', error);
            });
    }, [jwtToken]);

    const cancelAccountShowModal = (newValue) => {
        createAccountForm.resetFields();
        setValues(null)
        setSelectedAccountType(null)
        setSelectedAccountCurrency(null)
        setDeleteConfirmationVisible(false)
        setVisible(false)
    };

    const onDeleteAccount = (id) => {
        axios.get('/api/finance/v1/accounts/delete', {
            headers: {
                'Content-Type': 'application/json',
            },
            params: {
                accountId: id
            }
        })
            .then(response => {
                if (response.status == 200){
                    handleUpdateAccounts(accounts.filter(account => account.id !== response.data.account.id));
                    notification.success({
                        message: 'Успешно',
                        description: 'Аккаунт ' + response.data.account.name + ' удален!',
                    });
                    setDeleteConfirmationVisible(false)
                }
            })
            .catch(error => {
                notification.error({
                    message: 'Ошибка!',
                    description: 'Ошибка удаления аккаунта',
                });
                console.error('Ошибка удаления аккаунта:', error);
            });
    }

    const openAccountDeleteConfirm = () => {
        setVisible(false)
        setDeleteConfirmationVisible(true)
    }



    useEffect(() => {
        if (initialVisible) {
            createAccountForm.resetFields();
            if (initialValues && initialValues.type){
                setSelectedAccountType(initialValues.type)
                setSelectedAccountCurrency(initialValues.currency.isoCode)
            }

        }
    }, [initialVisible]);



    const handleOk = (newVal) => {
        if (initialValues){
            createAccountForm
                .validateFields()
                .then((values) => {
                    JSON.stringify(values);
                    axios.put('/api/finance/v1/accounts/edit', values, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': jwtToken,
                        },
                    }).then((response) => {
                        notification.success({
                            message: 'Данные изменены' ,
                            description: 'Счет ' + response.data.name +' данные изменины ',
                        })
                        createAccountForm.resetFields();
                        setVisible(false);
                        handleUpdateAccounts( accounts.map(account => {
                            if (account.id === response.data.id) {
                                return response.data;
                            } else {
                                return account;
                            }
                        }))

                    }).catch((error) => {
                        console.error('Ошибка:', error.response.data.error);
                        notification.error({
                            message: 'Ошибка!',
                            description: error.response.data.error,
                        });
                    });
                })
                .catch((errorInfo) => {
                console.log('Validation failed:', errorInfo);
            });
        } else {
            createAccountForm
                .validateFields()
                .then((values) => {
                     JSON.stringify(values);
                    axios.post('/api/finance/v1/accounts/create', values, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': jwtToken,
                        },
                    })
                        .then((response) => {
                            console.log('Response:', response.data);
                            notification.success({
                                message: 'Счет создан!' ,
                                description: 'Счет ' +response.data.name +' успешно создан в валюте ' + response.data.currency.isoCode,
                            });
                            createAccountForm.resetFields();
                            setVisible(false);
                            handleUpdateAccounts(accounts.concat(response.data))
                        })
                        .catch((error) => {
                            console.error('Ошибка:', error);
                            notification.error({
                                message: 'Ошибка!',
                                description: error.response.data,
                            });
                        });
                })
                .catch((errorInfo) => {
                    console.log('Validation failed:', errorInfo);
                });
        }

    }


    return (
        <div>


        <Modal
            key={Math.random()}
            className={"custom-card-modal"}
            title="Создать счет"
            visible={initialVisible}
            onOk={handleOk}
            onCancel={cancelAccountShowModal}
            footer={[
                <Button key="cancel" onClick={cancelAccountShowModal}>
                    Отмена
                </Button>,
                initialValues && (
                    <Button type="primary" key="delete" danger onClick={openAccountDeleteConfirm}>
                        Удалить аккаунт
                    </Button>
                ),
                <Button key="submit" type="primary" onClick={handleOk}>
                    Сохранить
                </Button>,

            ]}
        >
            <Form
                form={createAccountForm}
                name="basic"
                initialValues={initialValues ? {
                    name: initialValues.name,
                    currency: initialValues.currency.isoCode,
                    description: initialValues.description,
                    type: initialValues.type,
                    archive: initialValues.archive,
                    id: initialValues.id,
                    balance: initialValues.balance
                } : { remember: false}}
                layout="horizontal"
            >
                {initialValues ?
                    (<Form.Item
                        label="Id"
                        name="id"
                        hidden
                    >
                        <Input />
                    </Form.Item>) : (null)}
                <Form.Item
                    label="Имя"
                    name="name"
                    rules={[{ required: true, message: 'Введите имя!' }]}
                    labelCol={{ span: 6 }} // Ширина метки
                    wrapperCol={{ span: 18 }} // Ширина поля ввода
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label="Тип"
                    name="type"
                    rules={[{ required: true, message: 'Выбрать тип!' }]}
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 18 }}
                >
                    <Select onChange={handleAccountTypeChange} disabled={disabledEdit} >
                        {accountTypeList.map(type => (
                            <Option key={type.type} value={type.type}>
                                {type.name}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>
                {selectedAccountType=='CURRENCY' ? (
                    <Form.Item
                        label="Валюта"
                        name="currency"
                        rules={[{ required: true, message: 'Выберите валюту!' }]}
                        labelCol={{ span: 6 }}
                        wrapperCol={{ span: 18 }}
                    >
                        <Select disabled={disabledEdit} defaultValue={defaultCurrencyValue}>
                            {currencyList.map(currency => (
                                <Option key={currency.isoCode} value={currency.isoCode}>
                                    {currency.isoCode}
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>
                ) : (null)}
                {initialValues && selectedAccountType=='CURRENCY' ? (
                    <Form.Item
                        label="Баланс"
                        name="balance"
                        labelCol={{ span: 6 }}
                        wrapperCol={{ span: 18 }}
                    >
                        <InputNumber  step="0.01" style={{width: '100%'}} addonAfter={selectedAccountCurrency} defaultValue={100} disabled={disabledEdit} />
                    </Form.Item>
                ) :  (null)}
                <Form.Item
                    label="Описание"
                    name="description"
                    rules={[{ required: false, message: 'Введите описание!' }]}
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 18 }}
                >
                    <Input.TextArea  autoSize={{ minRows: 3, maxRows: 6 }} />
                </Form.Item>
                {initialValues ? (
                    <Form.Item label="Архивный" name="archive"
                               labelCol={{ span: 6 }}
                               wrapperCol={{ span: 18 }}>
                        <Switch />
                    </Form.Item>
                ) : (null)}
            </Form>
        </Modal>
            <DeleteConfirmationModal
                visible={deleteConfirmationVisible}
                onCancel={cancelAccountShowModal}
                onConfirm={onDeleteAccount}
                initialValues={initialValues}
            />
        </div>
    );
};

export default CreateAccountModal;