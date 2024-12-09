package com.bombulis.accounting.entity;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "currency")
public class Acc_Currency implements Serializable {
    /*USD("United States Dollar", "USD", "840"),
    RUB("Russian Ruble", "RUB", "643"),
    EUR("Euro", "EUR", "978"),
    AED("United Arab Emirates Dirham", "AED", "784"),
    CAD("Canadian Dollar", "CAD", "124"),
    JPY("Japanese Yen", "JPY", "392"),
    GBP("British Pound Sterling", "GBP", "826"),
    AUD("Australian Dollar", "AUD", "036"),
    CHF("Swiss Franc", "CHF", "756"),
    CNY("Chinese Yuan", "CNY", "156"),
    INR("Indian Rupee", "INR", "356"),
    SEK("Swedish Krona", "SEK", "752"),
    NZD("New Zealand Dollar", "NZD", "554"),
    ZAR("South African Rand", "ZAR", "710"),
    BRL("Brazilian Real", "BRL", "986"),
    SGD("Singapore Dollar", "SGD", "702");*/

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    private String fullName;
    @Column(unique = true, nullable = false, updatable = false)
    private String isoCode;
    @Column(unique = true, nullable = false, updatable = false)
    private String numericCode;

    @OneToMany(mappedBy = "currency",  fetch = FetchType.LAZY)
    private List<Acc_CurrencyAccount> currencyAccount;

    @OneToMany(mappedBy = "currency",  fetch = FetchType.LAZY)
    private List<Acc_SecurityPositionAccount> currencySecurity;


    @OneToMany(mappedBy = "currency", fetch = FetchType.LAZY)
    private List<Acc_User> user;

    @OneToMany(mappedBy = "currencyOperation", fetch = FetchType.LAZY)
    private List<Acc_EquityTransactionAccount> transactionAccounts;

    @Transient
    private double exchangeRate;

    Acc_Currency(String fullName, String isoCode, String numericCode) {
        this.fullName = fullName;
        this.isoCode = isoCode;
        this.numericCode = numericCode;
    }

    public Acc_Currency() {}

    public Acc_Currency(String fullName, String isoCode) {
        this.fullName = fullName;
        this.isoCode = isoCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Acc_Currency)) return false;
        Acc_Currency currency = (Acc_Currency) o;
        return isoCode.equals(currency.isoCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isoCode);
    }

    public String getFullName() {
        return fullName;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getNumericCode() {
        return numericCode;
    }
}
