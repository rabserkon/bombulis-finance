package com.bombulis.accounting.config;

import com.bombulis.accounting.repository.Acc_CurrencyRepository;
import com.bombulis.accounting.repository.Acc_TransactionRepository;
import com.bombulis.accounting.service.AccountService.Acc_AccountIntegrationService;
import com.bombulis.accounting.service.AccountService.Acc_AccountService;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyServiceImpl;
import com.bombulis.accounting.service.TransactionService.Acc_TransactionIntegrationService;
import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionProcessorFactory;
import com.bombulis.accounting.service.UserService.Acc_UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class Acc_GrpcServerConfig {

    @Value("${gRPC.port}")
    private int gRPCPort;

    @Autowired
    private Acc_CurrencyRepository currencyRepository;

    @Autowired
    private Acc_AccountService accountService;

    @Autowired
    private Acc_TransactionRepository transactionRepository;

    @Autowired
    private Acc_UserService userService;

    @Autowired
    private Acc_TransactionProcessorFactory transactionProcessor;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public Server grpcServer() {
        Acc_CurrencyServiceImpl currencyService = new Acc_CurrencyServiceImpl();
        currencyService.setCurrencyRepository(currencyRepository);
        Acc_AccountIntegrationService accountIntegrationService = new Acc_AccountIntegrationService();
        accountIntegrationService.setAccountService(accountService);
        Acc_TransactionIntegrationService transactionIntegrationService = new Acc_TransactionIntegrationService();
        transactionIntegrationService.setTransactionRepository(transactionRepository);
        transactionIntegrationService.setTransactionProcessorFactory(transactionProcessor);
        transactionIntegrationService.setUserService(userService);
        return ServerBuilder.forPort(gRPCPort)
                .addService(currencyService)
                .addService(accountIntegrationService)
                .addService(transactionIntegrationService)
                .build();
    }

    @Bean
    public GrpcServerRunner grpcServerRunner(Server grpcServer) {
        return new GrpcServerRunner(grpcServer);
    }

    public static class GrpcServerRunner implements CommandLineRunner {

        private final Server grpcServer;

        private final Logger logger = LoggerFactory.getLogger(getClass());

        public GrpcServerRunner(Server grpcServer) {
            this.grpcServer = grpcServer;
        }

        @Override
        public void run(String... args) throws Exception {
            grpcServer.start();
            logger.warn("gRPC Server started on port: " + grpcServer.getPort());
        }
    }
}
