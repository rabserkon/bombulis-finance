import React from 'react';
import Layout from "antd/es/layout/layout";
import UserBar from "../components/accouting/User/UserBar";
import FinanceSpace from "../components/accouting/FinanceSpace";
import TransactionCard from "../components/accouting/TransactionCard/TransactionCard";
import {Flex} from "antd";
import AccountBalance from "../components/accouting/Balance/AccountBalance";
import AssetsList from "../components/accouting/AssetsList/AssetsList";
import AccountList from "../components/accouting/AccountList/AccountList";
import axios from "axios";

function AccountingPage({currencyList, isDesktop}) {

    return (
        <Layout style={{minHeight: '100vh'}} >
            <UserBar/>
            <FinanceSpace isDesktop={isDesktop} currencyList={currencyList}></FinanceSpace>
        </Layout>
    );
}

export async function getServerSideProps ({ req, res}) {
    const userAgent = req.headers['user-agent']
    const isDesktop = !userAgent.match(/(iPhone|iPod|iPad|Android|BlackBerry)/);
    try {
        let currency = null
        return {
            props: {
                isDesktop: isDesktop,
                currencyList: currency,
            },
        };

    } catch (error) {
        return {
            props: {
                currencyList: null,
            },
        };
    }
}


export default AccountingPage;