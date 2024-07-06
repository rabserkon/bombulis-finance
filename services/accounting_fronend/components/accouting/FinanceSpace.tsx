import React from 'react';
import { Flex} from "antd";
import "./AccountList/accouting-content.css"
import AccountBalance from "./Balance/AccountBalance";
import AssetsList from "./AssetsList/AssetsList";
import AccountList from "./AccountList/AccountList";
import TransactionCard from "./TransactionCard/TransactionCard";

const transactionsData = [
    {
        id: 1,
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        sendAmount: 500.25,
        transactionDate: "2024-05-08T10:30:00",
        exchangeRate: 1.25,
        receivedAmount: 625.31,
        type: "EXCHANGE",
        recipientAccount: {
            id: 22,
            name: "Xffwer",
            create_at: "2024-05-05T19:14:29.444+00:00",
            lastUpdate_at: "2024-05-08T18:44:00.176+00:00",
            deleted: false,
            archive: false,
            type: "CURRENCY",
            description: "o[pkpk[p",
            currency: {
                id: 3,
                fullName: "Russian Ruble",
                isoCode: "RUB",
                numericCode: "643"
            },
        },
        senderAccount: {
            id: 20,
            name: "Xhbdh777",
            create_at: "2024-05-05T19:14:29.444+00:00",
            lastUpdate_at: "2024-05-08T18:44:00.176+00:00",
            deleted: false,
            archive: false,
            type: "CURRENCY",
            description: "o[pkpk[p",
            currency: {
                id: 3,
                fullName: "Russian Ruble",
                isoCode: "USD",
                numericCode: "643"
            },
        }
    },

];


function FinanceSpace({currencyList, currenciesRates, accountTypes}){
    return(
        <Flex wrap="wrap" style={
            { padding: '12px',

                alignContent: 'flex-start',
                alignItems: 'normal'
            }
        }>
            <AccountBalance />
            <AssetsList />
            <AccountList currencyList={currencyList} accountTypes={accountTypes}/>
            <TransactionCard transactions={transactionsData}/>
        </Flex>
    )
};

export default FinanceSpace;