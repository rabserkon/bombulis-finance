package com.bombulis.accounting.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan("com.bombulis.accounting")
@PropertySource("classpath:application.properties")
@EnableAutoConfiguration
public class ModuleConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }


    public ModuleConfig(Environment environment) throws IOException {
        String activeProfile = environment.getActiveProfiles()[0]; // Assuming a single active profile
        String propertyFileName = "application-" + activeProfile + ".properties";

        Resource resource = new ClassPathResource(propertyFileName);
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
    }

}
