package com.minecraftservers.app.service.KubernetesService;

import com.minecraftservers.app.service.KubernetesService.config.PodConfig;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.ApiResponse;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.Meta;
import io.kubernetes.client.proto.V1Networking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

public class IngressService {

    private CoreV1Api coreV1Api;

    @Value("k8s.namespace")
    private String namespace;

    private NetworkingV1Api networkingV1Api;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public IngressService() {
        this.networkingV1Api = new NetworkingV1Api();;
    }

    public IngressService(CoreV1Api coreV1Api) {
        this.coreV1Api = coreV1Api;
    }

    public void createOrUpdateIngress(PodConfig config) throws ApiException {

        // Параметры
        String namespace = config.getNamespace();
        String ingressName = config.getIngressName();
        String host = config.getHost();
        String serviceName = config.getServiceName();
        int servicePort = 80;
        String path = "/";

        // Создание метаданных
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName(ingressName);
        metadata.setNamespace(namespace);
        metadata.setAnnotations(Collections.singletonMap(
                "nginx.ingress.kubernetes.io/rewrite-target", "/"
        ));

        // Настройка backend
        V1IngressServiceBackend serviceBackend = new V1IngressServiceBackend();
        serviceBackend.setName(serviceName);
        serviceBackend.setPort(new V1ServiceBackendPort().number(80));

        // Настройка path
        V1HTTPIngressPath ingressPath = new V1HTTPIngressPath();
        ingressPath.setPath(path);
        ingressPath.setPathType("Prefix");
        ingressPath.setBackend(new V1IngressBackend().service(serviceBackend));

        // Настройка rule
        V1HTTPIngressRuleValue httpRule = new V1HTTPIngressRuleValue();
        httpRule.setPaths(Collections.singletonList(ingressPath));

        V1IngressRule rule = new V1IngressRule();
        rule.setHost(host);
        rule.setHttp(httpRule);

        V1IngressSpec spec = new V1IngressSpec();
        spec.setRules(Collections.singletonList(rule));

        V1Ingress ingress = new V1Ingress();
        ingress.setMetadata(metadata);
        ingress.setSpec(spec);

        V1Ingress result = networkingV1Api.createNamespacedIngress(
                namespace, // Имя namespace
                ingress, // Сам Ingress объект
                null, // Опциональные параметры
                null, // Опциональные параметры
                null, // Опциональные параметры
                null
        );
        System.out.println("Ingress успешно создан: " + ingressName);
    }

    protected boolean checkIngressStatus(String ingressName) throws KubernetesServiceException {
        try {
            V1Ingress ingressStatus = networkingV1Api.readNamespacedIngressStatus(
                    ingressName,  // Имя пода
                    namespace,                         // Пространство имен
                    null
            );

            if ("Running".equals(ingressStatus.getStatus())) {

            } else {
                return false;
            }

        } catch (ApiException e) {
            logger.info(e.getResponseBody());
            throw new KubernetesServiceException(e.getResponseBody());
        }
        return false;
    }
}
