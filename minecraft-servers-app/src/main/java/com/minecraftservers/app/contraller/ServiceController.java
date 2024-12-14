package com.minecraftservers.app.contraller;

import com.minecraftservers.app.service.ConsumerService.event.MinecraftServerCreateEvent;
import com.minecraftservers.app.service.KubernetesService.KubernetesService;
import com.minecraftservers.app.service.KubernetesService.MinecraftServerMangerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private KubernetesService kubernetesService;

    @Autowired
    private MinecraftServerMangerService minecraftServerMangerService;

    @GetMapping("/create-pod-minecraft-server")
    public void createTestPodMinecraftServer() throws Exception {
        MinecraftServerCreateEvent event = new MinecraftServerCreateEvent();
        event.setCpu("500");
        event.setName("test");
        event.setMemory("1024");
        event.setUserCount(5);
        event.setUuid(UUID.randomUUID().toString());
        event.setVersion("1.21.1");
        kubernetesService.createPod(event);
    }

    @GetMapping("/create-minecraft-server")
    public void createTestMinecraftServer() throws Exception {
        MinecraftServerCreateEvent event = new MinecraftServerCreateEvent();
        event.setCpu("150");
        event.setName("test");
        event.setMemory("1024");
        event.setUserCount(5);
        event.setUuid(UUID.randomUUID().toString());
        event.setVersion("1.21.1");
        minecraftServerMangerService.createMinecraftServer(event);

    }
}
