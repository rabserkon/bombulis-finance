package com.social.network.authentication.module.dto;

import com.social.network.authentication.module.service.JWTControlService.SessionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

@AllArgsConstructor
public class JwtSessionDTO {
    @Getter @Setter
    private String sessionUUID;
    @Getter @Setter
    private Date createTime;
    @Getter @Setter
    private Date updateTime;
    @Getter @Setter
    private boolean active;
    @Getter @Setter
    private Long userId;
    @Getter @Setter
    private String sessionHash;
    @Getter @Setter
    private String device;


    public JwtSessionDTO(SessionInfo sessionInfo) {
        this.sessionUUID = "[Crypted]";
        this.createTime = sessionInfo.getCreateTime();
        this.active = sessionInfo.isActive();
        this.userId = sessionInfo.getUserId();
        this.updateTime = sessionInfo.getUpdateTime();
        this.device = sessionInfo.getDevice();
        this.sessionHash = getChatHash(sessionInfo.getSessionUUID());
    }

    public static String getChatHash(String uuid) {
        try {
            String[] uuids = {uuid};
            Arrays.sort(uuids);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(uuids[0].getBytes());

            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashBuilder.append(String.format("%02x", b));
            }

            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e){
            return null;
        }
    }
}
