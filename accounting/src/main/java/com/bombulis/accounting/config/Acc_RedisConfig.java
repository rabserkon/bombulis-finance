package com.bombulis.accounting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
