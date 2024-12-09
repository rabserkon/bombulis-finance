package com.bombulis.financereport.repository;

import com.bombulis.financereport.documents.Rps_AccountReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Rps_AccountReportRepository extends MongoRepository<Rps_AccountReport, Long> {

}
