package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import com.bombulis.accounting.proto.AccountOuterClass;
import com.bombulis.accounting.proto.AccountServiceGrpc;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Acc_AccountIntegrationService extends AccountServiceGrpc.AccountServiceImplBase{


    private Acc_AccountService accountService;


    @Override
    public void getAccounts(AccountOuterClass.GetAccountsRequest request, StreamObserver<AccountOuterClass.GetAccountsResponse> responseObserver) {
        try {
            List<Acc_Account> accounts = (List<Acc_Account>) accountService.findUserAccounts(request.getUserId());

            List<AccountOuterClass.Account> protoAccounts = accounts.stream()
                    .filter((i) -> i.getType().equals(Acc_AccountType.CURRENCY.name().toUpperCase()))
                    .map((account) ->
                    {
                        Acc_CurrencyAccount currencyAccount = (Acc_CurrencyAccount) account;
                        return AccountOuterClass.Account.newBuilder()
                                .setId(account.getId())
                                .setName(account.getName())
                                .setArchive(account.isArchive())
                                .setDeleted(account.isDeleted())
                                .setType(account.getType())
                                .setBalance(currencyAccount.getBalance().toString())
                                .setCurrency(
                                        AccountOuterClass.AccCurrency.newBuilder()
                                                .setCurrencyCode(currencyAccount.getCurrency().getIsoCode())
                                                .setCurrencyName(currencyAccount.getCurrency().getFullName())
                                                .build()
                                ).build();
                    })
            .collect(Collectors.toList());
            AccountOuterClass.GetAccountsResponse response = AccountOuterClass.GetAccountsResponse.newBuilder()
                    .addAllAccounts(protoAccounts)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Acc_AccountNonFound acc_accountNonFound) {
            acc_accountNonFound.printStackTrace();
        }
    }

    @Override
    public void getAccountInfo(AccountOuterClass.GetAccountRequest request, StreamObserver<AccountOuterClass.GetAccountResponse> responseObserver) {
        try {
            Acc_Account account = accountService.findAccount(request.getAccountId(), request.getUserId());
            if (!account.getType().equals(Acc_AccountType.BROKERCURRENCY.name().toUpperCase())){
                throw new Acc_AccountOtherType("It's bad type");
            }
            Acc_CurrencyAccount currencyAccount = ((Acc_CurrencyAccount) account);
            AccountOuterClass.AccCurrency currencyResp = AccountOuterClass.AccCurrency.newBuilder()
                    .setCurrencyCode(currencyAccount.getCurrency().getIsoCode())
                    .setCurrencyName(currencyAccount.getCurrency().getFullName())
                    .build();
            AccountOuterClass.Account accountResp = AccountOuterClass.Account.newBuilder()
                    .setId(account.getId())  // ID счета
                    .setName(account.getName())  // Имя счета
                    .setArchive(account.isArchive())
                    .setDeleted(account.isDeleted())
                    .setType(account.getType())
                    .setBalance(currencyAccount.getBalance().toString())
                    .setCurrency(currencyResp)
                    .build();
            AccountOuterClass.GetAccountResponse accountResponse = AccountOuterClass.GetAccountResponse.newBuilder()
                    .setAccount(accountResp)
                    .build();

            responseObserver.onNext(accountResponse);
            responseObserver.onCompleted();
        } catch (Acc_AccountNonFound | Acc_AccountOtherType e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("An unexpected error occurred.").asRuntimeException());
        }
    }

    public void setAccountService(Acc_AccountService accountService) {
        this.accountService = accountService;
    }
}
