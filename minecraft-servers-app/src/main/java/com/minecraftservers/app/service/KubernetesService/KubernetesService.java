package com.minecraftservers.app.service.KubernetesService;

import com.minecraftservers.app.service.ConsumerService.event.MinecraftServerCreateEvent;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class KubernetesService {

    private  CoreV1Api coreV1Api;

    private static final String NAMESPACE = "minecraft-server";

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public KubernetesService(CoreV1Api coreV1Api) {
        this.coreV1Api = coreV1Api;
    }

    public void createPod(MinecraftServerCreateEvent serverConfig) throws KubernetesServiceException {
        try {
            // Уникальный идентификатор сервера
            String serverUUID = serverConfig.getUuid();
            List<V1EnvVar> envVars = new ArrayList<>();
            envVars.add(new V1EnvVar().name("SERVER_NAME")
                    .value(serverConfig.getName()));
            envVars.add(new V1EnvVar().name("EULA").value("TRUE"));
            envVars.add(new V1EnvVar().name("VERSION").value(serverConfig.getVersion()));


            // Создание контейнера с динамическими ресурсами
            V1Container minecraftContainer = new V1Container()
                    .name("minecraft-server")
                    .image("itzg/minecraft-server") // Используем официальный образ Minecraft
                    .env(envVars)  // Передаем имя сервера в переменную окружения
                    .resources(new V1ResourceRequirements()
                            .requests(Collections.singletonMap("memory", new Quantity(serverConfig.getMemory() + "Mi")))
                            .requests(Collections.singletonMap("cpu", new Quantity(serverConfig.getCpu()+ "m")))
                            .limits(Map.of(
                                    "memory", new Quantity(2048 + "Mi"),  // Лимит по памяти
                                    "cpu", new Quantity(500 + "m")         // Лимит по CPU
                            )))
                    .ports(List.of(
                            new V1ContainerPort().containerPort(25565)
                    ));
            V1Pod pod = new V1Pod()
                    .metadata(new V1ObjectMeta()
                            .name("minecraft-server-" + serverUUID)  // Имя пода включает UUID сервера
                            .namespace(NAMESPACE))
                    .spec(new V1PodSpec()
                            .addContainersItem(minecraftContainer));

            coreV1Api.createNamespacedPod(NAMESPACE, pod, null, null, null, null);
        } catch (ApiException e) {
            logger.info(e.getResponseBody());
            throw new KubernetesServiceException(e.getResponseBody());
        }

    }


    @Async
    public CompletableFuture<Boolean> waitForPodToBeRunning(String podName) throws KubernetesServiceException {
        int retries = 0;
        int maxRetries = 5; // Максимальное количество попыток
        int delayInSeconds = 20; // Задержка между попытками
        int delayInBefore = 120; // Задержка перед началом проверки, например, 3 минуты

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        CompletableFuture<Void> delayBeforeCheck = CompletableFuture.runAsync(() -> {
            try {
                scheduler.schedule(() -> {}, delayInBefore, TimeUnit.SECONDS).get();  // Задержка в 3 минуты
            } catch (Exception e) {
                logger.error("Error in delay before starting checks", e);
            }
        });

        delayBeforeCheck.join();

        while (retries < maxRetries) {
            boolean isRunning = checkPodStatus(podName);
            if (isRunning) {
                logger.info("Pod "+podName+"is running and ready.");
                return CompletableFuture.completedFuture(true); // Под работает
            }

            retries++;
            logger.info("Waiting for pod "+podName+" to be running... Attempt " + retries + "/" + maxRetries);
            CompletableFuture<Void> delay = CompletableFuture.runAsync(() -> {
                try {
                    scheduler.schedule(() -> {}, delayInSeconds, TimeUnit.SECONDS).get(); // Задержка между попытками
                } catch (Exception e) {
                    logger.error("Error in delay between attempts", e);
                }
            });
            delay.join();
        }
        logger.warn("Pod "+podName+"did not become running after " + maxRetries + " attempts.");
        try {
            deletePod(podName);
        } catch (KubernetesServiceException e) {
            logger.error("Failed to delete pod after failed attempts", e);
        }

        return CompletableFuture.completedFuture(false);
    }

    public void deletePod(String podName) throws KubernetesServiceException {
        try {
            coreV1Api.deleteNamespacedPod(
                    podName, // Имя пода
                    NAMESPACE,                        // Пространство имен
                    null,                              // Учетные данные, если нужно
                    null,                              // Параметры пропуска
                    null,                              // Параметры удаления
                    null,                              // Стратегия удаления
                    null,                                // Метаданные
                    null
            );
            logger.info("Pod " + podName + " deleted successfully.");
        } catch (ApiException e) {
            logger.error("Failed to delete pod " + podName, e);
            throw new KubernetesServiceException("Error while deleting pod: " + e.getMessage(), e);
        }
    }


    protected boolean checkPodStatus(String podName) throws KubernetesServiceException {
        try {
            V1Pod podStatus = coreV1Api.readNamespacedPodStatus(
                    podName,  // Имя пода
                    NAMESPACE,                         // Пространство имен
                    null
            );

            String podPhase = podStatus.getStatus().getPhase();
            if ("Running".equals(podPhase)) {
                for (V1ContainerStatus containerStatus : podStatus.getStatus().getContainerStatuses()) {
                    // Если контейнер не в состоянии "Running", проверяем его перезапуски
                    if (containerStatus.getState().getRunning() == null) {
                        return false; // Контейнер не в состоянии "Running"
                    }
                    if (containerStatus.getRestartCount() > 3) {
                        return false; // Контейнер был перезапущен
                    }
                    return true;
                }
            } else {
                return false;
            }

        } catch (ApiException e) {
            logger.info(e.getResponseBody());
            throw new KubernetesServiceException(e.getResponseBody());
        }
        return false;
    }


    @Autowired
    public void setCoreV1Api(CoreV1Api coreV1Api) {
        this.coreV1Api = coreV1Api;
    }


}
