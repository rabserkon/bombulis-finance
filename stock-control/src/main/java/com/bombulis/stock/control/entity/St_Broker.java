package com.bombulis.stock.control.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "st_broker_account")
@AllArgsConstructor
@Getter @Setter
public class St_Broker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brokerAccountId;

    @Column(name = "userId", nullable = false, length = 12)
    private Long userId;

    private String name;

    private String country;

    private boolean deleted;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "broker")
    private List<St_Account> accountList;

    public St_Broker(Long userId, String name, String country) {
        this.userId = userId;
        this.name = name;
        this.country = country;
        this.deleted = false;
    }

    public St_Broker() {
        this.deleted = false;
    }


}
