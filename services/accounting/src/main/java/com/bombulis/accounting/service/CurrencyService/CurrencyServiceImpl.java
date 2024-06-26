package com.bombulis.accounting.service.CurrencyService;

import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.repository.CurrencyRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService{

    private CurrencyRepository currencyRepository;

    @Override
    public List<Currency> getAllCurrencies() {
        List<Currency> currencyList = currencyRepository.findAll();
        if (currencyList == null || currencyList.isEmpty()){
            return new ArrayList<>();
        }
        return currencyList;
    }


    @Override
    public Currency findCurrency (String currencyIsoCode) throws CurrencyNonFound {
        Currency currency = currencyRepository.findCurrencyByIsoCode(currencyIsoCode);
        if (currency == null ){
            throw new CurrencyNonFound("Currency not found:" + currencyIsoCode);
        }
        return currency;
    }

    @Autowired
    public void setCurrencyRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
}
