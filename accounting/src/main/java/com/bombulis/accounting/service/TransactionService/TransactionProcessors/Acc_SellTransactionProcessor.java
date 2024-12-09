package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_EquityTransactionDTO;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
public class Acc_SellTransactionProcessor implements Acc_TransactionProcessor{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Acc_AccountService accountService;
    @Autowired
    private Acc_AccountRepository accountRepository;
    @Autowired
    private Acc_TransactionRepository transactionRepository;

    @Override
    public Acc_Transaction processCreateTransaction(Acc_TransactionDTO transactionDTO, Acc_User user) throws Acc_AccountException {
        Acc_EquityTransactionDTO transactionDTO1 = (Acc_EquityTransactionDTO) transactionDTO;
        Acc_Account brokerCurrencyAccount = accountService.findAccount(transactionDTO1.getCurrencyAccountId(), user.getUserId());
        boolean isBrokerCurrencyAccount = brokerCurrencyAccount.getType().equals(Acc_AccountType.BROKERCURRENCY.name().toUpperCase());

        if (!isBrokerCurrencyAccount){
            throw new Acc_AccountException("Is not broker currency account");
        }

        if (brokerCurrencyAccount instanceof Acc_CurrencyAccount) {
            Acc_CurrencyAccount currencyAccount = (Acc_CurrencyAccount) brokerCurrencyAccount;
            currencyAccount.setBalance(currencyAccount.getBalance().add(transactionDTO.getSendAmount()));

            Acc_EquityTransactionAccount equityTransactionAccount = new Acc_EquityTransactionAccount();
            equityTransactionAccount.setTransactionDate(new Timestamp(transactionDTO1.getTransactionDate().getTime()));
            equityTransactionAccount.setDescription(transactionDTO1.getDescription());
            equityTransactionAccount.setType(Acc_TransactionType.SELL.name().toUpperCase());
            equityTransactionAccount.setUser(user);
            equityTransactionAccount.setSendAmount(transactionDTO1.getSendAmount());
            equityTransactionAccount.setCurrencyOperation(currencyAccount.getCurrency());
            equityTransactionAccount.setEquityAccount(transactionDTO1.getEquityAccountId());
            equityTransactionAccount.setBrokerAccountId(transactionDTO1.getBrokerAccountId());
            equityTransactionAccount.setRecipientAccount(brokerCurrencyAccount);
            accountRepository.save(currencyAccount);
            return transactionRepository.save(equityTransactionAccount);
        } else {
            throw new Acc_AccountException("Is not broker currency account");
        }
    }

    @Override
    public Acc_BalanceDTO processCalculateBalance(Acc_TransactionAccount transactionAccount, Acc_BalanceDTO balanceDTO) {
        return null;
    }

    @Override
    public Acc_TransactionType getType() {
        return Acc_TransactionType.SELL;
    }
}
