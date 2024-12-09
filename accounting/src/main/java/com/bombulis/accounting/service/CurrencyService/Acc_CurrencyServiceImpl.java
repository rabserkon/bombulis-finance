package com.bombulis.accounting.service.CurrencyService;

import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.proto.Currency;
import com.bombulis.accounting.proto.CurrencyServiceGrpc;
import com.bombulis.accounting.repository.Acc_CurrencyRepository;
import com.bombulis.accounting.repository.Acc_UserRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Acc_CurrencyServiceImpl extends CurrencyServiceGrpc.CurrencyServiceImplBase implements Acc_CurrencyService {

    private Acc_CurrencyRepository currencyRepository;
    private Acc_UserRepository userRepository;
    private final String MAIN_CURRENCY= "USD";

    @Override
    public void getCurrencyInfo(Currency.CurrencyRequest request, StreamObserver<Currency.CurrencyResponse> responseObserver) {
        String currencyCode = request.getIsoCode();
        try {
            Acc_Currency currency = findCurrency(currencyCode);
            Currency.CurrencyResponse response = Currency.CurrencyResponse.newBuilder()
                    .setCurrencyId(currency.getId().toString())
                    .setIsoCode(currency.getIsoCode())
                    .setNumericCode(currency.getNumericCode())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Acc_CurrencyNonFound e) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Валюта с кодом " + currencyCode + " не найдена.")
                    .asRuntimeException());
            return;
        }
    }

    @Override
    public List<Acc_Currency> getAllCurrencies() {
        List<Acc_Currency> currencyList = currencyRepository.findAll();
        if (currencyList == null || currencyList.isEmpty()){
            return new ArrayList<>();
        }
        return currencyList;
    }

    @Override
    public Acc_Currency getMainUserCurrency(Long userId) throws Acc_CurrencyNonFound {
        Acc_Currency currency = userRepository.findUserByUserId(userId).get().getCurrency();
        if (currency == null){
            return findCurrency(MAIN_CURRENCY);
        }
        return currency;
    }


    @Override
    public Acc_Currency findCurrency (String currencyIsoCode) throws Acc_CurrencyNonFound {
        Acc_Currency currency = currencyRepository.findCurrencyByIsoCode(currencyIsoCode)
                .orElseThrow(()-> new Acc_CurrencyNonFound("Currency with code " + currencyIsoCode + " not found"));
        return currency;
    }

    @Autowired
    public void setCurrencyRepository(Acc_CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void setUserRepository(Acc_UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
