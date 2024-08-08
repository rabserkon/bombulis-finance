package com.bombulis.accounting.dao;

import com.bombulis.accounting.model.dao.ConsolidateAccount;
import com.bombulis.accounting.model.dao.Transaction;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransactionDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    protected List<Transaction> findTransactionByAccountOnPeriod(
            Long accountId,
            LocalDateTime startPeriod,
            LocalDateTime endPeriod,
            TransactionType transactionType
    ) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT tr.id, tr.description, tr.sendamount, tr.transactiondate, tr.type, tr.exchangerate, ")
                .append("tr.receivedamount, tr.recipient_account_id, tr.sender_account_id, ")
                .append("a_sender.name, a_sender.type, ")
                .append("a_receiver.name, a_receiver.type ")
                .append("FROM transactionaccount tr ")
                .append("INNER JOIN account a_sender ON tr.sender_account_id = a_sender.id ")
                .append("INNER JOIN account a_receiver ON tr.recipient_account_id = a_receiver.id ")
                .append("WHERE tr.transactiondate BETWEEN :startDate AND :endDate ")
                .append("AND (tr.sender_account_id = :accountNumber OR tr.recipient_account_id = :accountNumber) ");
        if (transactionType != null) {
            queryBuilder.append("AND tr.type = :transactionType ");
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("accountNumber", accountId)
                .addValue("startDate", startPeriod)
                .addValue("endDate", endPeriod);

        if (transactionType != null) {
            parameters.addValue("transactionType", transactionType.name().toUpperCase());
        }
        return namedParameterJdbcTemplate.query(queryBuilder.toString(), parameters, new TransactionRowMapper());
    }


    protected List<ConsolidateAccount> consolidateAccountOperationOnPeriod(
            Long accountId,
            LocalDateTime startPeriod,
            LocalDateTime endPeriod
    ) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("WITH ")
                .append("received AS ( ")
                .append("    SELECT tr.recipient_account_id AS account_id, ")
                .append("           SUM(COALESCE(tr.receivedamount, tr.sendamount)) AS total_receive_amount, ")
                .append("           0 AS total_send_amount, ")
                .append("           0 AS balance_before_start_date ")
                .append("    FROM transactionaccount tr ")
                .append("    WHERE tr.transactiondate BETWEEN :startDate AND :endDate ")
                .append("      AND tr.recipient_account_id = :accountId ")
                .append("    GROUP BY tr.recipient_account_id ")
                .append("), ")
                .append("sent AS ( ")
                .append("    SELECT tr.sender_account_id AS account_id, ")
                .append("           0 AS total_receive_amount, ")
                .append("           SUM(tr.sendamount) AS total_send_amount, ")
                .append("           0 AS balance_before_start_date ")
                .append("    FROM transactionaccount tr ")
                .append("    WHERE tr.transactiondate BETWEEN :startDate AND :endDate ")
                .append("      AND tr.sender_account_id = :accountId ")
                .append("    GROUP BY tr.sender_account_id ")
                .append("), ")
                .append("balance_before_start AS ( ")
                .append("    SELECT :accountId AS account_id, ")
                .append("           SUM(CASE ")
                .append("               WHEN tr.recipient_account_id = :accountId THEN COALESCE(tr.receivedamount, tr.sendamount) ")
                .append("               ELSE 0 ")
                .append("           END) - SUM(CASE ")
                .append("               WHEN tr.sender_account_id = :accountId THEN tr.sendamount ")
                .append("               ELSE 0 ")
                .append("           END) AS balance_before_start_date ")
                .append("    FROM transactionaccount tr ")
                .append("    WHERE (tr.recipient_account_id = :accountId OR tr.sender_account_id = :accountId) ")
                .append("      AND tr.transactiondate < :startDate ")
                .append(") ")
                .append("SELECT :accountId AS account_id, ")
                .append("       SUM(received.total_receive_amount) AS total_receive_amount, ")
                .append("       SUM(sent.total_send_amount) AS total_send_amount, ")
                .append("       SUM(balance_before_start.balance_before_start_date) AS balance_before_start_date, ")
                .append("       SUM(balance_before_start.balance_before_start_date) + SUM(received.total_receive_amount) - SUM(sent.total_send_amount) AS final_balance ")
                .append("FROM received ")
                .append("FULL OUTER JOIN sent ON received.account_id = sent.account_id ")
                .append("FULL OUTER JOIN balance_before_start ON balance_before_start.account_id = COALESCE(received.account_id, sent.account_id) ")
                .append("GROUP BY COALESCE(received.account_id, sent.account_id, balance_before_start.account_id);");

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("accountId", accountId)
                .addValue("startDate", startPeriod)
                .addValue("endDate", endPeriod);

        return namedParameterJdbcTemplate.query(queryBuilder.toString(), parameters, new ConsolidateAccountRowMapper());
    }

    private static final class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getLong("id"));
            transaction.setDescription(rs.getString("description"));
            transaction.setSendAmount(rs.getBigDecimal("sendamount"));
            transaction.setTransactionDate(rs.getTimestamp("transactiondate").toLocalDateTime());
            transaction.setType(rs.getString("type"));
            transaction.setExchangeRate(rs.getBigDecimal("exchangerate"));
            transaction.setReceivedAmount(rs.getBigDecimal("receivedamount"));
            transaction.setReceivedAccountId(rs.getLong("recipient_account_id"));
            transaction.setSenderAccountId(rs.getLong("sender_account_id"));
            return transaction;
        }
    }

    public static final class ConsolidateAccountRowMapper implements RowMapper<ConsolidateAccount> {
        @Override
        public ConsolidateAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConsolidateAccount consolidateAccount = new ConsolidateAccount();
            consolidateAccount.setAccountId(rs.getLong("account_id"));
            consolidateAccount.setTotalReceiveAmount(rs.getBigDecimal("total_receive_amount"));
            consolidateAccount.setTotalSendAmount(rs.getBigDecimal("total_send_amount"));
            consolidateAccount.setBalanceBeforeStartPeriod(rs.getBigDecimal("balance_before_start_date"));
            consolidateAccount.setBalanceAfterEndPeriod(rs.getBigDecimal("final_balance"));
            return consolidateAccount;
        }
    }
}
