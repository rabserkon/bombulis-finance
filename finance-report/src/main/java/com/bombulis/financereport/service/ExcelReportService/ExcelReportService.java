package com.bombulis.financereport.service.ExcelReportService;

import com.bombulis.financereport.dto.Account;
import com.bombulis.financereport.dto.AccountReport;
import com.bombulis.financereport.dto.ConsolidateAccount;
import com.bombulis.financereport.dto.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelReportService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public byte[] generateExcelReport(AccountReport report) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Account Report");

            // Set page size to A4
            sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
            sheet.getPrintSetup().setFitWidth((short) 1); // Fit to 1 page wide
            sheet.getPrintSetup().setFitHeight((short) 0); // Do not limit height



            // Set header
            String headerText = String.format(
                    "Информация по счету № %d с %s по %s",
                    report.getAccount().getAccountId(),
                    report.getStatementPeriodStart(),
                    report.getConsolidateAccount().getBalanceAfterEndPeriod().format(FORMATTER)
            );

            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue(headerText);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6)); // Merge cells for header
            headerCell.setCellStyle(createHeaderStyle(workbook));

            // Account Information
            int rowNum = 2;
            Row accountInfoHeaderRow = sheet.createRow(rowNum++);
            accountInfoHeaderRow.createCell(0).setCellValue("Account Information:");
            accountInfoHeaderRow.getCell(0).setCellStyle(createSubHeaderStyle(workbook));

            createAccountInfoRows(sheet, rowNum, report.getAccount());

            // Consolidate Account
            rowNum += 7; // Leave space after account info
            Row consolidateHeaderRow = sheet.createRow(rowNum++);
            consolidateHeaderRow.createCell(0).setCellValue("Consolidated Account on this period:");
            consolidateHeaderRow.getCell(0).setCellStyle(createSubHeaderStyle(workbook));

            createConsolidateAccountRows(sheet, rowNum, report.getConsolidateAccount());

            // Transactions
            rowNum += 7; // Leave space after consolidate account
            Row transactionsHeaderRow = sheet.createRow(rowNum++);
            transactionsHeaderRow.createCell(0).setCellValue("Transactions:");
            transactionsHeaderRow.getCell(0).setCellStyle(createSubHeaderStyle(workbook));

            createTransactionRows(sheet, rowNum, report.getTransactions(), report.getAccount());

            // Set column widths
            for (int i = 0; i <= 8; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 256); // Add extra space for visibility
            }

            workbook.write(baos);
            return baos.toByteArray();
        }
    }

    private void createAccountInfoRows(Sheet sheet, int rowNum, Account account) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Account ID:");
        row.createCell(1).setCellValue(account.getAccountId().toString());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Name:");
        row.createCell(1).setCellValue(account.getName());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Currency:");
        row.createCell(1).setCellValue(account.getCurrency());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Description:");
        row.createCell(1).setCellValue(account.getDescription());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Type:");
        row.createCell(1).setCellValue(account.getType());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Sub Type:");
        row.createCell(1).setCellValue(account.getSubType());
    }

    private void createConsolidateAccountRows(Sheet sheet, int rowNum, ConsolidateAccount consolidateAccount) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Total Receive Amount:");
        row.createCell(1).setCellValue(consolidateAccount.getTotalReceiveAmount().toString());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Total Send Amount:");
        row.createCell(1).setCellValue(consolidateAccount.getTotalSendAmount().toString());

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Balance Before Start Period:");
        row.createCell(1).setCellValue(consolidateAccount.getBalanceBeforeStartPeriod().format(FORMATTER));

        row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("Balance After End Period:");
        row.createCell(1).setCellValue(consolidateAccount.getBalanceAfterEndPeriod().format(FORMATTER));
    }

    private void createTransactionRows(Sheet sheet, int rowNum, List<Transaction> transactions, Account account) {
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Sender Account ID");
        headerRow.createCell(2).setCellValue("Received Account ID");
        headerRow.createCell(3).setCellValue("Description");
        headerRow.createCell(4).setCellValue("Transaction Date");
        headerRow.createCell(5).setCellValue("Amount");
        headerRow.createCell(6).setCellValue("Currency");


        for (Transaction transaction : transactions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(transaction.getId().toString());
            row.createCell(1).setCellValue(transaction.getSenderAccountId().toString());
            row.createCell(2).setCellValue(transaction.getReceivedAccountId().toString());
            row.createCell(3).setCellValue(transaction.getDescription());
            row.createCell(4).setCellValue(transaction.getTransactionDate().format(FORMATTER));
            row.createCell(5).setCellValue(getAmountTransaction(transaction,account).toString());
            row.createCell(6).setCellValue(account.getCurrency());

        }
    }

    private StringBuilder getAmountTransaction(Transaction transaction, Account account){
        if (transaction.getSenderAccountId() == account.getAccountId()){
            BigDecimal amountMinus = transaction.getSendAmount();
            return new StringBuilder().append("- ").append(amountMinus.toString()).append(" " + account.getCurrency());
        } else if (transaction.getReceivedAccountId() == account.getAccountId()){
            BigDecimal amountPlus = transaction.getReceivedAmount() != null ? transaction.getReceivedAmount() : transaction.getSendAmount();
            return new StringBuilder().append("+ ").append(amountPlus.toString()).append(" " + account.getCurrency());
        } else {
            return new StringBuilder().append("bad operation");
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createSubHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }
}
