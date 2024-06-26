import {combineReducers, createStore, } from 'redux';
import { configureStore } from '@reduxjs/toolkit'
import accountsReducer from "../reducers/accountsReducer";
import currenciesReducer from "../reducers/currenciesReducer";

const rootReducer = combineReducers({
        accounts: accountsReducer,
        currencies: currenciesReducer,
});

const store = createStore(accountsReducer);

export default store;

