package com.bombulis.stock.control.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "st_stock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class St_Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Column(nullable = false)
    private String sTickerSymbol;

    @Column(nullable = false)
    private String sISIN;
}
