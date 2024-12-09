package com.bombulis.accounting.repository;

import com.bombulis.accounting.entity.Acc_Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Acc_CurrencyRepository extends CrudRepository<Acc_Currency,Long> {
    Optional<Acc_Currency> findCurrencyByIsoCode(String isoCode);
    Optional<Acc_Currency> findCurrencyByNumericCode(String numericCode);
    List<Acc_Currency> findAll();
}
