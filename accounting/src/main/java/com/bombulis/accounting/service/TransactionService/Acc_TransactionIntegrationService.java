package com.bombulis.accounting.service.TransactionService;

import com.bombulis.accounting.dto.Acc_EquityTransactionDTO;
import com.bombulis.accounting.entity.Acc_EquityTransactionAccount;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.proto.Transaction;
import com.bombulis.accounting.proto.TransactionServiceGrpc;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionProcessor;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionProcessorFactory;
import com.bombulis.accounting.service.UserService.Acc_UserService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class Acc_TransactionIntegrationService extends TransactionServiceGrpc.TransactionServiceImplBase {

    private Acc_TransactionRepository transactionRepository;
    private Acc_TransactionProcessorFactory transactionProcessorFactory;
    private Acc_UserService userService;

    @Override
    public void debit(Transaction.TransactionRequest request, StreamObserver<Transaction.TransactionResponse> responseObserver) {
        try {
            // Получаем тип транзакции
            String transactionType = request.getOperation().getValueDescriptor().getName();
            // Получаем процессор для обработки транзакции
            Acc_TransactionProcessor transactionProcessor = transactionProcessorFactory.getProcessor(transactionType.toUpperCase());
            // Создаем объект транзакции
            Acc_EquityTransactionDTO transactionDTO = new Acc_EquityTransactionDTO();
            transactionDTO.setTransactionDate(Date.from(Instant.ofEpochSecond(request.getDate().getSeconds(), request.getDate().getNanos())));
            transactionDTO.setType(transactionType.toUpperCase());
            transactionDTO.setDescription(request.getComment());
            transactionDTO.setSendAmount(new BigDecimal(request.getAmount()));
            transactionDTO.setCurrencyAccountId(Long.parseLong(request.getAccountId()));
            transactionDTO.setCurrencyCode(request.getCurrency());
            transactionDTO.setBrokerAccountId(Long.parseLong(request.getBrokerAccountId()));
            transactionDTO.setEquityAccountId(Long.parseLong(request.getEquityAccountId()));

            // Ищем пользователя по ID
            Acc_User user = userService.findUserById(Long.parseLong(request.getUserId()));
            if (user == null) {
                responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("User not found")));
                return;
            }
            // Обрабатываем транзакцию
            Acc_EquityTransactionAccount transaction = (Acc_EquityTransactionAccount) transactionProcessor.processCreateTransaction(transactionDTO, user);

            // Создаем успешный ответ
            Transaction.TransactionResponse response = Transaction.TransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Transaction processed successfully")
                    .build();

            // Отправляем успешный ответ
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NumberFormatException e) {
            // Обрабатываем ошибку парсинга UserId
            responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription("Invalid user ID format")));
        } catch (Exception e) {
            // Обрабатываем общие ошибки
            responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription("An internal error occurred: " + e.getMessage())));
        }

    }

    @Autowired
    public void setTransactionRepository(Acc_TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setTransactionProcessorFactory(Acc_TransactionProcessorFactory transactionProcessorFactory) {
        this.transactionProcessorFactory = transactionProcessorFactory;
    }

    @Autowired
    public void setUserService(Acc_UserService userService) {
        this.userService = userService;
    }
}
