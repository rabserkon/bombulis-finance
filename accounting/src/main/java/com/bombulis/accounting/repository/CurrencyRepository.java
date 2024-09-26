package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency,Long> {
    Optional<Currency> findCurrencyByIsoCode(String isoCode);
    Optional<Currency> findCurrencyByNumericCode(String numericCode);
    List<Currency> findAll();
}
