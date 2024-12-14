package com.minecraftservers.app.service.ConsumerService.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class MinecraftServerCreateEvent {
    private String uuid;
    private String name;
    private String version;
    private String cpu;
    private String memory;
    private int userCount;
}
