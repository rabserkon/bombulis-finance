package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.SecurityTransactionDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.repository.TransactionRepository;
import com.bombulis.accounting.repository.UserRepository;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.AccountService;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SecurityTransactionProcessor implements TransactionProcessor{

    private AccountService accountService;
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    @Override
    public TransactionAccount processCreateTransaction(TransactionDTO transactionDTO, User user) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException {
        SecurityTransactionDTO transaction = (SecurityTransactionDTO) transactionDTO;
        if (transaction.getTransactionType().equals(TransactionType.BUY)){
           /* SecurityPositionAccount securityAccount = accountRepository.findSecurityPositionAccountByUserUserIdAndDeletedFalseAndTicker(userId, transaction.getTicker());
            if (securityAccount ==  null){

            }*/

        }


        if (transaction.getTransactionType().equals(TransactionType.SELL)){

        }
        return null;
    }

    @Override
    public BalanceDTO processCalculateBalance(TransactionAccount transactionAccount, BalanceDTO balanceDTO) {
        return null;
    }

    @Override
    public TransactionType getType() {
        return null;
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    @Qualifier("accountServiceImpl")
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
