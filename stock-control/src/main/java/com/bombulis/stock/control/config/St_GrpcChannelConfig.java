package com.bombulis.stock.control.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class St_GrpcChannelConfig {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
    }

    @PreDestroy
    public void shutdownChannel() {
        managedChannel().shutdown();
    }
}
