package com.bombulis.financereport.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan("com.bombulis.financereport")
@EnableAutoConfiguration
public class ModuleConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    public ModuleConfig(@Value("${spring.profiles.active}") String activeProfile) throws IOException {
        String propertyFileName = "application-" + activeProfile + ".properties";
        Resource resource = new ClassPathResource(propertyFileName);
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
    }
}
