package com.bombulis.accounting.service.ReportService;

import com.bombulis.accounting.model.dao.Acc_AccountReportEvent;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;

import java.time.LocalDateTime;

public interface Acc_ReportService {

    Acc_AccountReportEvent createAccountReport(Long accountId,
                                               Long userId,
                                               LocalDateTime startPeriod,
                                               LocalDateTime endPeriod) throws Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_AccountOtherType;
}
