package com.bombulis.stock.control.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "st_currency")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class St_Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currencyId; // id

    @Column(nullable = false)
    private String sName;

    @Column(unique = true, nullable = false, updatable = false)
    private String currencyCode;

    @Column(unique = true, nullable = false, updatable = false)
    private String numericCode;
}
