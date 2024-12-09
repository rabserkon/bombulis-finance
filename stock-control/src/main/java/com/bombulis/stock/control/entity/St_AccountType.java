package com.bombulis.stock.control.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "st_account_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class St_AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id", nullable = false)
    private Long typeId;

    private String type;

    private Boolean active;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

}