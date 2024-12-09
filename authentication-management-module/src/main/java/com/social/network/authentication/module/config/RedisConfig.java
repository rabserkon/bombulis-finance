package com.social.network.authentication.module.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SslOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import io.lettuce.core.resource.DefaultClientResources;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;


import java.io.File;
import java.io.IOException;
import java.time.Duration;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Value("${spring.redis.ssl}")
    private boolean sslEnabled;

    private ResourceLoader resourceLoader;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Bean
    @Profile("prod")
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        return factory;
    }

    @Bean
    @Profile("dev")
    public RedisConnectionFactory redisConnectionFactoryDev() throws IOException {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setPassword(redisPassword);
        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder =
                LettuceClientConfiguration.builder();
        LettuceClientConfiguration lettuceClientConfiguration = lettuceClientConfigurationBuilder.build();
        return new LettuceConnectionFactory(config, lettuceClientConfiguration);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws IOException {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
