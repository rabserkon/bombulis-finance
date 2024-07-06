import React, {useEffect, useState} from 'react';
import {Table, Button, Tag, notification, Space, Typography, DatePicker} from 'antd';
import moment from "moment";
import axios from "axios";
import TransactionDrawer from "./TransactionDrawer";
const { Paragraph, Text, Title } = Typography;
import {useDispatch, useSelector} from "react-redux";

const TransactionCard = () => {
    const accounts = useSelector(state => state.accounts);
    const transactions = useSelector(state => state.transactional)
    const [transactionsList, setTransactionsList] = useState(transactions);
    const [drawerVisible, setDrawerVisible] = useState(false);
    const tags = {
        BUY: {
            description: "Покупка",
            color: "green",
            operation: "BUY"
        },
        SELL: {
            description: "Продажа",
            color: "red",
            operation: "SELL"
        },
        EXCHANGE: {
            description: "Обмен",
            color: "yellow",
            operation: "EXCHANGE"
        },
        DEFAULT : {
            description: "Перевод",
            color: "blue",
            operation: "DEFAULT"
        }
    };

    const filterType = [
        {
            text: "Покупка",
            value: "BUY"
        },
        {
            text: "Продажа",
            value: "SELL"
        },
        {
            text: "Обмен",
            value: "EXCHANGE"
        },
        {
            text: "Перевод",
            value: "DEFAULT"
    }];

    useEffect(() =>{
        axios.get('/api/finance/v1/transaction/search', {
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                const newTransactions = response.data.transactions;
                const mergedTransactions = [ ...newTransactions];
                const sortedTransactions = mergedTransactions.sort((a, b) => a.id - b.id);
                const transactionsWithKeys = sortedTransactions.map((transaction, index) => ({
                    ...transaction,
                    key: transaction.id,
                }));
                setTransactionsList(transactionsWithKeys);
            })
            .catch(error => {
                notification.error({
                    message: 'Ошибка!',
                    description: 'Ошибка загрузки транзакций',
                });
                console.error('Ошибка загрузки транзакций:', error);
            });
    }, [])


    useEffect(()=>{
        setTransactionsList(transactions)
    }, [transactions])

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
            render: () => null,
            sorter: (a, b) => a.id - b.id,
            sortOrder: 'descend',
            hidden: true
        },
        {
            title: 'Тип',
            dataIndex: 'type',
            key: 'type',
            render: (sendAmount, record) => (
                <p style={{height: 'auto' , margin: '0'}}>
                    {tags[record.type].description}
                </p>
            ),
        },
        {
            title: 'Описание',
            dataIndex: 'description',
            key: 'description',
            render: (sendAmount, record) => (
                <p style={{height: 'auto' , margin: '0'}}>
                    {record.description ? record.description : 'Нет описания'}
                </p>
            ),
        },
        {
            title: 'Отправленная сумма',
            dataIndex: 'sendamount',
            key: 'sendamount',
            render: (sendAmount, record) => (
                <span>{record.sendAmount.toFixed(2)} {record.senderAccount.currency.isoCode} </span>
            ),
        },
        {
            title: 'Полученная сумма',
            dataIndex: 'receivedamount',
            key: 'receivedamount',
            render: (sendAmount, record) => (

                <span>{record.receivedAmount ? (record.receivedAmount.toFixed(2)) : (record.sendAmount.toFixed(2))} {record.recipientAccount ? (record.recipientAccount.currency.isoCode ) : (record.senderAccount.currency.isoCode)} </span>
            ),
        },
        {
            title: 'Дата транзакции',
            dataIndex: 'transactionDate',
            key: 'transactionDate',
            render:  (sendAmount, record) => (moment(new Date(record.transactionDate)).format('YYYY-MM-DD')),
            sorter: (a, b) => moment(new Date(a.transactionDate)).unix() - moment(new Date(b.transactionDate)).unix(),
            defaultSortOrder: 'descend',
        },
        {
            title: 'Операция',
            dataIndex: 'type',
            key: 'type',
            render: (sendAmount, record) => (
                <span>
        {tags[record.type] && (
            <Tag bordered={false} color={tags[record.type].color} key={tags[record.type].operation}>
                {tags[record.type].operation.toUpperCase()}
            </Tag>
        )}
    </span>
            ),
            filters: filterType,
            onFilter: (value, record) => record.type.indexOf(value) === 0,
        },
        {
            title: 'Подробнее',
            key: 'operation',
            fixed: 'right',
            render: (text, record) => (
                <Button type="link" onClick={() => handleViewDetails(record)}>
                    Информация
                </Button>
            ),
        },

    ];

    const handleViewDetails = (transaction) => {

    };


    const expandedRowRender = (record) => {
        const isBuyTransaction = record.type === "BUY";

        return (
            <div style={{ display: 'flex' }}>
                <div style={{
                    marginLeft: '50px',
                    marginBottom: '10px',
                    marginTop: '-10px'
                }}>
                    <Space direction="vertical">
                        <Text strong>ID транзакции:</Text>
                        <Text>{record.id}</Text>
                        <Text strong>Описание:</Text>
                        <Paragraph>{record.description || "Отсутствует"}</Paragraph>
                        <Text strong>Сумма перевода:</Text>
                        <Text> {record.sendAmount.toFixed(2)} {record.senderAccount.currency.isoCode} {"списано с "} {record.senderAccount.name} {record.exchangeRate ? ("") : ("зачислено на " + record.recipientAccount.name)} </Text>
                        <Text strong>Дата транзакции:</Text>
                        <Text>{moment(record.transactionDate).format('DD.MM.YYYY hh:mm')}</Text>
                        {record.exchangeRate ?  (
                            <>
                                <Text strong>Курс обмена:</Text>
                                <Text>{record.exchangeRate.toFixed(2)}</Text>
                                <Text strong>Полученная сумма:</Text>
                                <Text> {record.receivedAmount.toFixed(2)} {record.recipientAccount.currency.isoCode} {"зачислено на"} {record.recipientAccount.name} </Text>
                            </>
                        ) : (null)}
                        <Text strong>Тип:</Text>
                        <Tag color={record.type === 'BUY' ? 'green' : 'blue'}>{record.type}</Tag>
                    </Space>
                </div>

                {isBuyTransaction && (
                    <div style={{ flex: 1 }}>

                    </div>
                )}
            </div>
        );
    }


    function onDeposit() {

    }

    function onWithdraw() {

    }


    const handleDateRangeChange = (dates) => {
        if (dates){
            const formattedDates = dates.map(date => moment(new Date(date)).format('YYYY-MM-DD'));
            axios.get('/api/finance/v1/transaction/search', {
                headers: {
                    'Content-Type': 'application/json',
                },
                params: {
                    from: formattedDates[0],
                    to: formattedDates[1],
                }
            })
                .then(response => {
                    const newTransactions = response.data.transactions;
                    const mergedTransactions = [ ...newTransactions];
                    const sortedTransactions = mergedTransactions.sort((a, b) => a.id - b.id);
                    const transactionsWithKeys = sortedTransactions.map((transaction, index) => ({
                        ...transaction,
                        key: transaction.id,
                    }));
                    setTransactionsList(transactionsWithKeys);
                })
                .catch(error => {
                    notification.error({
                        message: 'Ошибка!',
                        description: 'Ошибка загрузки транзакций',
                    });
                    console.error('Ошибка загрузки транзакций:', error);
                });
        }
    };

    function onNewTransaction() {
        setDrawerVisible(true)
    }

    return (
        <div title="" className={'custom-card'} bordered={false} style={{
            width: '100%'}}>
     {/*       <Title style={{
                marginLeft: '10px',
                marginBottom: '15px'
            }} level={2} >Транзакции</Title>*/}
            <div style={{ marginBottom: '16px',  marginLeft: '10px', }}>
                <Space  style={{ marginTop: '20px', marginBottom: '20px' }}>
                    <Button type="primary" onClick={onDeposit}>Депозит</Button>
                    <Button type="primary" onClick={onWithdraw}>Вывод</Button>
                    <Button type="primary" onClick={onNewTransaction}>Новая транзакция</Button>
                </Space>
                <Space style={{ marginLeft: '16px' }}>
                    <DatePicker.RangePicker onChange={handleDateRangeChange} />
                </Space>
            </div>
                <Table
                    style={{
                        width: '100%'}}
                    columns={columns}
                    dataSource={transactionsList}
                    rowkey={(record) => record.id}
                    rowKey="id"
                    pagination={{
                        position: ['none','bottomCenter'],
                    }}
                    expandable={{
                        expandedRowRender,
                    }}
                />
                <TransactionDrawer visible={drawerVisible} onClose={setDrawerVisible}/>
        </div>
    );
}

export default TransactionCard;