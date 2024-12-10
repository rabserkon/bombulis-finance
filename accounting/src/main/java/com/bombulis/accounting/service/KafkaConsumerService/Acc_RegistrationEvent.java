package com.bombulis.accounting.service.KafkaConsumerService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Acc_RegistrationEvent implements Serializable {
    private Long userId;
    private String version;

    public Acc_RegistrationEvent() {
    }

    @JsonCreator
    public Acc_RegistrationEvent(@JsonProperty("userId") String userId, @JsonProperty("version") String version) {
        this.userId = Long.parseLong(userId);
        this.version = version;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
