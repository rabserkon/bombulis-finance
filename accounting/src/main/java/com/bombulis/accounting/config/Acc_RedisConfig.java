package com.bombulis.accounting.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SslOptions;
import io.lettuce.core.protocol.ProtocolVersion;
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
public class Acc_RedisConfig {
  /*  @Value("${spring.redis.host}")
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
    RedisConnectionFactory redisConnectionFactoryProd() throws IOException {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setPassword(redisPassword);
        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder =
                LettuceClientConfiguration.builder();
        if (sslEnabled){
            SslOptions sslOptions = SslOptions.builder().jdkSslProvider()
                    .trustManager(new File("/redis/redis.pem"))
                    .build();
            ClientOptions clientOptions = ClientOptions.builder()
                    .sslOptions(sslOptions)
                    .protocolVersion(ProtocolVersion.RESP3)
                    .pingBeforeActivateConnection(false)
                    .build();

            lettuceClientConfigurationBuilder
                    .clientOptions(clientOptions)
                    .commandTimeout(Duration.ofSeconds(5))
                    .shutdownTimeout(Duration.ofMillis(100))
                    .useSsl();
            System.out.println("Redis ssl user:" + sslEnabled);
        }
        LettuceClientConfiguration lettuceClientConfiguration = lettuceClientConfigurationBuilder.build();
        return new LettuceConnectionFactory(config, lettuceClientConfiguration);
    }

    @Bean
    @Profile("dev")
    RedisConnectionFactory redisConnectionFactoryDev() throws IOException {
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
    }*/
}
