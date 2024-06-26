package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency,Long> {
    Currency findCurrencyByIsoCode(String isoCode);
    Currency findCurrencyByNumericCode(String numericCode);
    List<Currency> findAll();
}
