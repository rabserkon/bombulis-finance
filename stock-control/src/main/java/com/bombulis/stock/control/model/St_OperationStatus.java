package com.bombulis.stock.control.model;

public enum St_OperationStatus  {
    PENDING(100),              // Ожидается выполнение
    PROCESSING(200),           // В процессе выполнения
    COMPLETED(300),            // Успешно завершена
    FAILED(400),               // Ошибка
    CANCELLED(500),            // Отменена
    REJECTED(600),             // Отклонена
    EXPIRED(700),              // Истек срок действия
    PARTIALLY_COMPLETED(800),  // Частично выполнена
    AWAITING_SETTLEMENT(900),  // Ожидает расчетов
    SETTLED(1000);             // Расчеты завершены

    private final int code;

    St_OperationStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
