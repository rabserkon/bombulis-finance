package com.bombulis.accounting.service.CurrencyService;

import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.repository.CurrencyRepository;
import com.bombulis.accounting.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService{

    private CurrencyRepository currencyRepository;
    private UserRepository userRepository;
    private final String MAIN_CURRENCY= "USD";

    @Override
    public List<Currency> getAllCurrencies() {
        List<Currency> currencyList = currencyRepository.findAll();
        if (currencyList == null || currencyList.isEmpty()){
            return new ArrayList<>();
        }
        return currencyList;
    }

    @Override
    public Currency getMainUserCurrency(Long userId) throws CurrencyNonFound {
        Currency currency = userRepository.findUserByUserId(userId).get().getCurrency();
        if (currency == null){
            return findCurrency(MAIN_CURRENCY);
        }
        return currency;
    }


    @Override
    public Currency findCurrency (String currencyIsoCode) throws CurrencyNonFound {
        Currency currency = currencyRepository.findCurrencyByIsoCode(currencyIsoCode)
                .orElseThrow(()-> new CurrencyNonFound("Currency with code " + currencyIsoCode + " not found"));
        return currency;
    }

    @Autowired
    public void setCurrencyRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
}
