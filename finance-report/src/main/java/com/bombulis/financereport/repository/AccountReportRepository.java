package com.bombulis.financereport.repository;

import com.bombulis.financereport.documents.AccountReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountReportRepository extends MongoRepository<AccountReport, Long> {

}
