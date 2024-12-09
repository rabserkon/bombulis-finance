package com.bombulis.stock.control.model;

import com.bombulis.stock.control.entity.St_Broker;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class St_BrokerAccountDTO {
    @NotNull
    private String name;
    @NotNull
    private String country;
    private Long id;

    public St_BrokerAccountDTO(St_Broker broker) {
        this.id=broker.getBrokerAccountId();
        this.name = broker.getName();
        this.country = broker.getCountry();
    }
}
