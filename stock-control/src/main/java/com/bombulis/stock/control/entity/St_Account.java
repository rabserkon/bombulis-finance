package com.bombulis.stock.control.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "st_account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class St_Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "l_userId", nullable = false, length = 12)
    private Long userId; // Связь с пользователем

    @ManyToOne(optional = false)
    @JoinColumn(name = "s_account_type_ID", nullable = false)
    private St_AccountType accountType;

    @Column(name = "s_isin", nullable = false, length = 12)
    private String isin;

    @Column(name = "s_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "s_currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "s_price", nullable = false)
    private BigDecimal price;

    @Column(name = "s_total_value", nullable = false)
    private BigDecimal totalValue;

    @Column(name = "s_creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "s_last_operation_date")
    private LocalDateTime lastOperationDate;

    @Column(nullable = false)
    private String companyName; // Компания

    @Column
    private BigDecimal profit; // Результат

    @ManyToOne(fetch = FetchType.EAGER)
    private St_Broker broker;

    @OneToMany(fetch = FetchType.LAZY)
    private List<St_Operation> operationList;

    @Override
    public String toString() {
        return "St_Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", accountType=" + accountType.getType() +
                ", isin='" + isin + '\'' +
                ", quantity=" + quantity +
                ", currency='" + currency + '\'' +
                ", price=" + price +
                ", totalValue=" + totalValue +
                ", creationDate=" + creationDate +
                ", lastOperationDate=" + lastOperationDate +
                ", companyName='" + companyName + '\'' +
                ", broker=" + broker.getBrokerAccountId() +
                '}';
    }
}
