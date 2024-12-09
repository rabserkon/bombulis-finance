package com.bombulis.stock.control.model;

import com.bombulis.stock.control.service.AccountService.St_AccountException;

import java.util.Map;

public enum St_OperationType {
    BUY(0),
    SELL(1);

    private final int value;

    St_OperationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<String, St_OperationType> TYPE_MAP = Map.of(
            "BUY", BUY,
            "SELL", SELL
    );

    public static St_OperationType fromString(String type) throws St_AccountException {
        St_OperationType operationType = TYPE_MAP.get(type);
        if (operationType == null) {
            throw new St_AccountException("Invalid operation type: " + type);
        }
        return operationType;
    }
}
