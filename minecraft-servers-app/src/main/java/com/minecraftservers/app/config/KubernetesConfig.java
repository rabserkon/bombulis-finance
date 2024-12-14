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

    String kubeApiServerUrl = "https://kubernetes.docker.internal:6443";
    String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkJDZkxwV21YNHo3Vy1DUkthSWdjSVNpbVBEOXVMS3hNcHF6Q1lWS3J6cnMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJkZWZhdWx0LXRva2VuLXAyZmNrIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImRlZmF1bHQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI3MDA4ZjY1Yi1hNzYyLTRlMzItYjZiYS01N2NkOTFlZTk4NzgiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZS1zeXN0ZW06ZGVmYXVsdCJ9.o8CnkZNd4HoplU1I-dVYnSC2DEhUc5uXbQgUT9_CcagsGAa4OI1_1E3_0AfXT48wQyX6h59PKGwkRsYiyhl9J2BsDKwafz4dFAAS7xp_dJyb5eqRfVEBtL-PCkOiua1TJeSdvOBDEbMkF7jSejnTqMsrTo59V39do849NWBIAtVXt9qOi3KTV9j3YjXsaM7tCpT-jpFUl33aSSLfwl1UyqOVolKdEZFn5KNqMJ5UkM10m5QYEe8va5b5iHM3HE7YIDQsizZzkR6Zz_s_iIHcLFBkr4bjQer2F2mltGM737wrygE0rqBZZ12_knh4CznXDIHI7WXN1Shfw-iyRJPXwQ";

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
