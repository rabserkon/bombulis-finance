package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.repository.AccountRepository;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransactionProcessor implements TransactionProcessor{

    private AccountService accountService;

    private AccountRepository accountRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public TransactionAccount processCreateTransaction(TransactionDTO transactionDTO, User user) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException {
        Account senderAccount = accountService.findAccount(transactionDTO.getSenderAccountId(), user.getUserId());
        Account receiverAccount = accountService.findAccount(transactionDTO.getReceivedAccountId(), user.getUserId());

        if (senderAccount.equals(receiverAccount)) {
            throw new AccountNonFound("Accounts are equal");
        }

        if (senderAccount instanceof CurrencyAccount && receiverAccount instanceof CurrencyAccount) {
            CurrencyAccount senderCurrencyAccount = (CurrencyAccount) senderAccount;
            CurrencyAccount receiverCurrencyAccount = (CurrencyAccount) receiverAccount;
            TransactionAccount transactionAccount = new TransactionAccount(
                    transactionDTO.getDescription(),
                    transactionDTO.getSendAmount(),
                    senderAccount,
                    receiverAccount,
                    transactionDTO.getTransactionDate(),
                    user,
                    TransactionType.DEFAULT.name());
            senderCurrencyAccount.setBalance(senderCurrencyAccount.getBalance().subtract(transactionDTO.getSendAmount()));
            receiverCurrencyAccount.setBalance(receiverCurrencyAccount.getBalance().add(transactionDTO.getSendAmount()));
            accountRepository.save(senderCurrencyAccount);
            accountRepository.save(receiverCurrencyAccount);
            return transactionAccount;
        } else {
            throw new IllegalArgumentException("Один или оба аккаунта не являются CurrencyAccount");
        }
    }

    @Override
    public BalanceDTO processCalculateBalance(TransactionAccount transaction, BalanceDTO balanceDTO) {
        final long accountId = balanceDTO.getAccountId();
        if (transaction.getRecipientAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().add(transaction.getSendAmount()));
        } else if (transaction.getSenderAccount().getId().equals(accountId)) {
            balanceDTO.setBalance(balanceDTO.getBalance().subtract(transaction.getSendAmount()));
        }

        return balanceDTO;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEFAULT;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
