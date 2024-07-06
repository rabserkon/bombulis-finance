import React, {useEffect} from 'react';
import Layout from "antd/es/layout/layout";
import UserBar from "../components/accouting/User/UserBar";
import FinanceSpace from "../components/accouting/FinanceSpace";
import axios from "axios";
import Cookies from 'cookies'
import {useDispatch} from "react-redux";



function AccountingPage({currencyList, accountTypes, accountList, transactionalList}) {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch({ type: 'SET_ACCOUNTS', payload: accountList });
        dispatch({ type: 'SET_TRANSACTIONAL' , payload: transactionalList})
    }, [accountList, dispatch]);


    return (
        <Layout style={{minHeight: '100vh'}} >
            <UserBar/>
            <FinanceSpace accountTypes={accountTypes} currencyList={currencyList}></FinanceSpace>
        </Layout>
    );
}

export async function getServerSideProps ({ req, res}) {
    const userAgent = req.headers['user-agent']
    const cookies = new Cookies(req, res);
    const jwtToken = cookies.get('jwtToken');
    const isDesktop = !userAgent.match(/(iPhone|iPod|iPad|Android|BlackBerry)/);

    const response = await axios.get('http://localhost:8080/service/dashboard/information', {
        method: 'GET',
        headers: {
            Authorization: 'Bearer ' + jwtToken
        }
    });
    const data = response.data;
    return {
        props: {
            currencyList: data.currencies,
            currencyRate: data.currencies_rates,
            accountTypes: data.account_types,
            transactionalList: data.transactional_list
        },
    };
}


export default AccountingPage;