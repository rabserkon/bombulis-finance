const initialState = {
    currencies: [],
};

const accountsReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_ACCOUNTS':
            return {
                ...state,
                currencies: action.payload,
            };
        default:
            return state;
    }
};

export default accountsReducer;