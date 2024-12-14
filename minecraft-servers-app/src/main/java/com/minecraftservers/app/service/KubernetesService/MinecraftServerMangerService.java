package com.minecraftservers.app.service.KubernetesService;

import com.minecraftservers.app.service.ConsumerService.event.MinecraftServerCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MinecraftServerMangerService {

    private KubernetesService kubernetesService;
    private MessageService messageService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void createMinecraftServer(MinecraftServerCreateEvent serverConfig) throws KubernetesServiceException {
        final String serverUUIDName = "minecraft-server-" + serverConfig.getUuid();
        kubernetesService.createPod(serverConfig);
        try {
            CompletableFuture<Boolean> podStatusFuture = kubernetesService.waitForPodToBeRunning(serverUUIDName);
            podStatusFuture.thenAccept(isRunning -> {
                if (isRunning) {
                    messageService.sendNotification("Minecraft server is up and running!");
                } else {
                    messageService.sendNotification("Minecraft server failed to start.");
                }
            }).exceptionally(ex -> {
                logger.error("Error while waiting for pod to be running", ex);
                messageService.sendNotification("Error while waiting for Minecraft server to start.");
                return null;
            });
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            messageService.sendNotification("Unexpected error occurred while starting Minecraft server.");
        }
    }



    @Autowired
    public MinecraftServerMangerService(KubernetesService kubernetesService, MessageService messageService) {
        this.kubernetesService = kubernetesService;
        this.messageService = messageService;
    }
}
