package com.bombulis.stock.control.config;

import com.bombulis.stock.control.service.OperationService.St_CurrencyService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan("com.bombulis.stock.control")
@EnableAutoConfiguration
public class St_ModuleConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    public St_ModuleConfig(@Value("${spring.profiles.active}") String activeProfile) throws IOException {
        String propertyFileName = "application-" + activeProfile + ".properties";
        Resource resource = new ClassPathResource(propertyFileName);
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
    }



    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
