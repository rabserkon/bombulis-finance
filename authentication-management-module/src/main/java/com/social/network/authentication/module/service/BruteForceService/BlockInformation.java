package com.social.network.authentication.module.service.BruteForceService;

import lombok.Getter;

import java.util.Date;

public class BlockInformation {
    @Getter
    private long userId;
    @Getter
    private Date blockData;
    @Getter
    private int blockTimeInSecond;

    public BlockInformation(long userId, int blockTimeInSecond) {
        this.userId = userId;
        this.blockData = new Date();
        this.blockTimeInSecond = blockTimeInSecond;
    }
}
