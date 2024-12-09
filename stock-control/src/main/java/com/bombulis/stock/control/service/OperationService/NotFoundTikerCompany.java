package com.bombulis.stock.control.service.OperationService;

public class NotFoundTikerCompany extends Exception {

    public NotFoundTikerCompany(String message) {
        super(message);
    }

    public NotFoundTikerCompany(String message, Throwable cause) {
        super(message, cause);
    }
}
