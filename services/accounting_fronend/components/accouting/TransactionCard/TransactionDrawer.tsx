import {Drawer, Form, Button, Select, Input, InputNumber, DatePicker} from 'antd';
import {useEffect, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import './transaction-style.css'
import axios from "axios";

const { Option } = Select;

const TransactionDrawer = ({ visible, onClose, }) => {
    const dispatch = useDispatch();
    const [form] = Form.useForm();
    const accounts = useSelector(state => state.accounts);
    const [showExchangeFields, setShowExchangeFields] = useState(false);
    const [senderAccountId, setSenderAccountId] = useState(null);
    const [receivedAccountId, setReceivedAccountId] = useState(null);
    const [receivedAmount, setReceivedAmount] = useState(1.00);
    const [sendAmount, setSendAmount] = useState(null);
    const [exchangeRate, setExchangeRate] = useState(1.00)
    const [type, setType] = useState('DEFAULT');

    const handleAccountChange = (value) => {
        const currencies = value.map((accountId) => accounts.find((account) => account.id === accountId).currency.isoCode);
        setShowExchangeFields(new Set(currencies).size > 1);
    };

    const handleFinish = (values) => {
        console.log('Submitted values:', values);
        if (values)
        axios.post('/api/finance/v1/transaction/create', values, {
            headers: {
                'Content-Type': 'application/json',
            },
        }).then(response => {
            let transaction = response.data.transaction;
            dispatch(addTransaction(transaction));
            const senderAccount = accounts.find(acc => acc.id === transaction.senderAccount.id);
            const recipientAccount = accounts.find(acc => acc.id === transaction.recipientAccount.id);
            if (senderAccount && recipientAccount ) {
                const updatedSenderAccount = {
                    ...senderAccount,
                    balance: senderAccount.balance - transaction.sendAmount
                };
                const updatedRecipientAccount = {
                    ...recipientAccount,
                    balance: recipientAccount.balance + (transaction.receivedAmount != null ? transaction.receivedAmount : transaction.sendAmount)
                };
                dispatch(updateAccount(updatedSenderAccount))
                dispatch(updateAccount(updatedRecipientAccount))
                console.log(updatedRecipientAccount, updatedSenderAccount)
            }
        }).catch(error => {

        })
        onClose();
    };

    const addTransaction = (transactionData) => ({
        type: 'ADD_TRANSACTION',
        payload: transactionData
    });

    const updateAccount = (updatedAccount) => ({
        type: 'UPDATE_ACCOUNT',
        payload: updatedAccount
    });

    const onCloseThis = () => {
        onClose(false)
    };
    const resetTabs = () => {
        setType('DEFAULT')
        onClose(false)
        form.resetFields()
        setShowExchangeFields(false)
        setExchangeRate(1.00)
        setType('DEFAULT')
        setSenderAccountId(null);
        setSendAmount(null)
        setReceivedAmount(null)
    };

    const handleSenderAccountChange = (value) => {
        setSenderAccountId(value);
    };

    const handleReceivedAccountChange = (value) => {
        setReceivedAccountId(value);
    };

    const changeExchangeRate = (value) => {
    }

    useEffect(() => {
        if (senderAccountId && receivedAccountId){
            const senderAccount = accounts.find(account => account.id === senderAccountId);
            const receivedAccount = accounts.find(account => account.id === receivedAccountId);
            if (senderAccount.currency.isoCode !== receivedAccount.currency.isoCode) {
                setShowExchangeFields(true)
                setType('EXCHANGE')
                form.setFieldsValue({type: 'EXCHANGE'})
            } else {
                setType('DEFAULT')
                form.setFieldsValue({type: 'DEFAULT'})
                setShowExchangeFields(false)
            }
        }

    },[senderAccountId,receivedAccountId])

    const handleChangeExchangeRate = (val) =>{
        setExchangeRate(val)
    }
    const handleSendAmount = (val) =>{
        setSendAmount(val)
    }


    useEffect(() => {
        let changeAmount = (sendAmount * exchangeRate)
        setReceivedAmount(changeAmount)
        form.setFieldsValue({ receivedAmount: changeAmount.toFixed(2) });
    }, [sendAmount, exchangeRate])

    useEffect(() => {
        let changeRate = (receivedAmount / sendAmount)
        setExchangeRate(changeRate)
        form.setFieldsValue({ exchangeRate: changeRate.toFixed(2) });
    }, [receivedAmount])

    return (
        <Drawer
            title="Создать транзакцию"
            placement="bottom"
            height="80%"
            closable={false}
            onClose={onCloseThis}
            visible={visible}
            style={{
            }}
            footer={
                <div
                    style={{
                        textAlign: 'right',
                    }}
                >
                    <Button onClick={resetTabs} style={{ marginRight: 8 }}>
                        Отмена
                    </Button>
                    <Button onClick={() => form.submit()} type="primary">
                        Создать
                    </Button>
                </div>
            }
        >
            {accounts ? (
                <Form
                    initialValues={{
                        type: 'DEFAULT'
                    }}
                    form={form}
                    layout="vertical"
                    onFinish={handleFinish}
                    className={"transaction-form"}
                >
                    <Form.Item name="type" hidden>
                        <Input value={type} />
                    </Form.Item>
                    <Form.Item
                        name="senderAccountId"
                        label="Счет списания"
                        rules={[{ required: true, message: 'Выберите счет' }]}
                    >
                        <Select value={senderAccountId} onChange={handleSenderAccountChange}>
                            {accounts.map(account => (
                                <Option key={account.id} value={account.id}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                                        <span>{account.name}</span>
                                        <span>{account.balance.toFixed(2)} {account.currency.isoCode}</span>
                                    </div>
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>
                    <Form.Item
                        name="receivedAccountId"
                        label="Счет зачисления"
                        rules={[{ required: true, message: 'Выберите счет' }]}
                    >
                        <Select value={receivedAccountId} onChange={handleReceivedAccountChange}>
                            {accounts.map(account => (
                                <Option key={account.id} value={account.id}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                                        <span>{account.name}</span>
                                        <span>{account.balance.toFixed(2)} {account.currency.isoCode}</span>
                                    </div>
                                </Option>
                            ))}
                        </Select>
                    </Form.Item>
                    <Form.Item rules={[{ required: true, message: 'Выберите дату' }]} label="Дата операции" name="transactionDate">
                        <DatePicker style={{width: '100%' }} />
                    </Form.Item>
                    <Form.Item
                        name="sendAmount"
                        label="Сумма операции"
                        rules={[{ required: true, message: 'Выберите сумму' }]}
                    >
                        <InputNumber  onChange={handleSendAmount} style={{width: '100%' }}
                                      addonAfter={senderAccountId  ? accounts.find(account => account.id === senderAccountId).currency.isoCode : ''}
                                      defaultValue={0}
                                      step="0.01"/>
                    </Form.Item>

                     {showExchangeFields && (
                    <>
                        <Form.Item
                            name="exchangeRate"
                            label="Курс обмена"
                            rules={[{ required: true, message: 'Введите курс обмена' }]}
                        >
                            <InputNumber step="0.01" onChange={handleChangeExchangeRate} style={{ width: '100%' }} />
                        </Form.Item>
                        <Form.Item

                            name="receivedAmount"
                            label="Сумма зачисления"
                            rules={[{ required: true, message: 'Введите сумму зачисления' }]}
                        >
                            <InputNumber
                                         style={{width: '100%' }}
                                         onChange={setReceivedAmount}
                                         addonAfter={accounts.find(account => account.id === receivedAccountId).currency.isoCode}
                                         step="0.01"/>
                        </Form.Item>
                    </>
                )}
                    <Form.Item label="Описание" name="description">
                        <Input.TextArea  autoSize={{ minRows: 3, maxRows: 6 }} />
                    </Form.Item>
                </Form>
            ) : (null) }
        </Drawer>
    );
};

export default TransactionDrawer;