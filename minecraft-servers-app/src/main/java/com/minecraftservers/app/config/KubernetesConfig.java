package com.minecraftservers.app.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.auth.ApiKeyAuth;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.FileReader;

@Configuration
public class KubernetesConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    @Profile("prod")
    public ApiClient apiClientProd() throws Exception {
        ApiClient client = Config.defaultClient();
        client.setDebugging(false);
        return client;
    }

    @Bean
    @Profile("dev")
    public ApiClient apiClientDev() throws Exception {
        String kubeConfigPath = System.getProperty("user.home") + "\\.kube\\config";
        File file = new File(kubeConfigPath);
        if (!file.exists()) {
            System.out.println("Файл config не найден: " + kubeConfigPath);
        } else {
            logger.info("K8s config exists");
            logger.info("K8s config path file: " + kubeConfigPath);
        }
        ApiClient client = Config.fromConfig(kubeConfigPath);
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
        ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

       /* ApiClient client = Config.defaultClient();
        client.setBasePath(kubeApiServerUrl);

        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) client.getAuthentication("BearerToken");
        apiKeyAuth.setApiKey(token);
        apiKeyAuth.setApiKeyPrefix("Bearer");

        // Устанавливаем клиент по умолчанию
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);*/
        return client;
    }

    @Bean
    public CoreV1Api coreV1Api(ApiClient apiClient) {
        return new CoreV1Api(apiClient);
    }

}
