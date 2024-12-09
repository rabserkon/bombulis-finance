package com.bombulis.stock.control.controller;

import com.bombulis.stock.control.component.St_MultiAuthToken;
import com.bombulis.stock.control.entity.St_Operation;
import com.bombulis.stock.control.model.St_OperationDTO;
import com.bombulis.stock.control.service.AccountService.St_AccountException;
import com.bombulis.stock.control.service.OperationService.NotFoundTikerCompany;
import com.bombulis.stock.control.service.OperationService.St_OperationService;
import com.bombulis.stock.control.service.OperationService.TransactionExceptionAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/operation")
@Validated
public class St_OperationController {

    @Autowired
    private St_OperationService operationService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createOperation(Authentication auth,
                                                        @Valid @RequestBody St_OperationDTO operationStock) throws IOException, NotFoundTikerCompany, St_AccountException, TransactionExceptionAccountService {
        St_Operation createOperation = operationService.createOperation(operationStock, ((St_MultiAuthToken) auth).getUserId());
        Map<String,Object> response = new HashMap<>();
        response.put("operation", new St_OperationDTO(createOperation));
        return  ResponseEntity.ok(response);
    }
}
