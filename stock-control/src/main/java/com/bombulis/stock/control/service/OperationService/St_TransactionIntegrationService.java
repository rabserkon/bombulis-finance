package com.bombulis.stock.control.service.OperationService;

import com.bombulis.stock.control.entity.St_Operation;
import com.bombulis.stock.control.model.St_OperationType;
import com.bombulis.stock.control.proto.Transaction;
import com.bombulis.stock.control.proto.TransactionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class St_TransactionIntegrationService extends TransactionServiceGrpc.TransactionServiceImplBase {

    private final RestTemplate restTemplate;
    private final ManagedChannel channel;
    private final TransactionServiceGrpc.TransactionServiceBlockingStub stub;

    @Autowired
    public St_TransactionIntegrationService(ManagedChannel channel, RestTemplate restTemplate) {
        this.channel = channel;
        this.restTemplate = restTemplate;
        this.stub = TransactionServiceGrpc.newBlockingStub(channel);
    }


    public St_Operation sendTransactionToAccount(St_Operation operation) throws TransactionExceptionAccountService {
        try {
            // Формируем строку комментария на английском языке
            String comment = String.format(
                    "Operation %s: %s of security with ISIN %s, quantity %d at price %.2f for amount %.2f in %s from account id: %s",
                    operation.getOperationId(),                             // Operation ID
                    operation.getType(), // Operation type (buy/sell)
                    operation.getAccount().getIsin(),                           // ISIN of the security
                    operation.getQuantity(),                               // Quantity of securities
                    operation.getPrice().doubleValue(),                    // Price per unit
                    operation.getAmount().doubleValue(),
                    operation.getCurrency(),
                    operation.getCurrencyAccountId()                       // Account number
            );

            // Создаем запрос
            Transaction.TransactionRequest request = Transaction.TransactionRequest.newBuilder()
                    .setAccountId(operation.getCurrencyAccountId().toString())
                    .setCurrency(operation.getCurrencyCode())
                    .setAmount(operation.getAmount().doubleValue())
                    .setComment(comment)
                    .setUserId(operation.getUserId().toString())
                    .setOperationValue(St_OperationType.fromString(operation.getType()).getValue())
                    .setBrokerAccountId(operation.getBroker().getBrokerAccountId().toString())
                    .setEquityAccountId(operation.getAccount().getAccountId().toString())
                    .build();

            // Отправляем запрос
            Transaction.TransactionResponse response = this.stub.debit(request);

            // Обрабатываем ответ
            if (response.getSuccess()) {



                System.out.println("Transaction completed successfully: " + response.getMessage());
                return operation;
            } else {
                throw new TransactionExceptionAccountService("Error during transaction: " + response.getMessage());

            }
        } catch (Exception e) {
            System.err.println("An error occurred while sending the transaction: " + e.getMessage());
            e.printStackTrace();
            throw new TransactionExceptionAccountService("Error during transaction: " + e.getMessage());

        }
    }

}
