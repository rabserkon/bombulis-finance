package com.minecraftservers.app.service.KubernetesService.config;

import io.kubernetes.client.openapi.models.V1EnvVar;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PodConfig {
    private final String containerName;
    private final String imageName;
    private final String namespace;
    private final String serviceName;

    private String ingressName;
    private String host;

    private List<V1EnvVar> envVars = new ArrayList<>();

    public PodConfig(String containerName, String imageName, String namespace, String serviceName) {
        this.containerName = containerName;
        this.imageName = imageName;
        this.namespace = namespace;
        this.serviceName = serviceName;
    }
}
