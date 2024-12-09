package com.bombulis.stock.control.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "st_operations")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class St_Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id; // id операции

    private String operationId;

    @Column(nullable = false)
    private BigDecimal amount; // Сумма операции

    @Column(nullable = false)
    private BigDecimal price; // Цена

    @Column(nullable = false)
    private Integer quantity; // Количество

    @Column(nullable = false)
    private String currency; // Тип валюты

    @Column(nullable = false)
    private String type; // Тип операции

    @Column(nullable = false)
    private String status; // Состояние операции

    @Column(nullable = false)
    private Integer MCStatus;

    @Column(nullable = false)
    private Long userId; // id пользователя

    @Column(nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Long currencyAccountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId", nullable = false, updatable = false)
    private St_Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brokerAccountId", nullable = false, updatable = false)
    private St_Broker broker;

}
