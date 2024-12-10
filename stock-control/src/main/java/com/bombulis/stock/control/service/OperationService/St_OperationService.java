package com.bombulis.stock.control.service.OperationService;

import com.bombulis.stock.control.entity.St_Operation;
import com.bombulis.stock.control.model.St_OperationDTO;
import com.bombulis.stock.control.service.AccountService.St_AccountException;
import com.bombulis.stock.control.service.AccountService.St_AccountNotFoundException;

public interface St_OperationService {

    St_Operation createOperation(St_OperationDTO operationForm, Long userId) throws St_AccountNotFoundException, NotFoundTikerCompany, St_AccountException, TransactionExceptionAccountService;
}
