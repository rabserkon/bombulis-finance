const initialState = {
    accounts: [],
    transactional: []
};

const accountsReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_TRANSACTIONAL':
            return {
                ...state,
                transactional: action.payload,
            };
        case 'SET_ACCOUNTS':
            return {
                ...state,
                accounts: action.payload,
            };
        default:
            return state;
        case 'UPDATE_ACCOUNT':
            const accountIdToUpdate = action.payload.id;
            const updatedAccountIndex = state.accounts.findIndex(account => account.id === accountIdToUpdate);
            const updatedAccounts = [
                ...state.accounts.slice(0, updatedAccountIndex),
                {
                    ...state.accounts[updatedAccountIndex],
                    ...action.payload
                },
                ...state.accounts.slice(updatedAccountIndex + 1)
            ];
            return {
                ...state,
                accounts: updatedAccounts
            };
        case 'ADD_TRANSACTION':
            return {
                ...state,
                transactional: [...state.transactional, action.payload]
            };
        case 'GET_ACCOUNT':
            const accountId = action.payload;
            const account = state.accounts.find(account => account.id === accountId);
            return {
                ...state,
                selectedAccount: account
            };
    }
};

export default accountsReducer;