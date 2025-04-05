/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 */
package com.daimler.cebas.certificates.control.vo;

import io.swagger.annotations.ApiModel;

@ApiModel
public class UpdateCertificatesRetryInfo {
    private String endpoint;
    private Integer maxRetries;
    private Integer currentRetry;
    private Integer nextRetryTime;
    private long nextRetryTimestamp;

    public UpdateCertificatesRetryInfo() {
    }

    public UpdateCertificatesRetryInfo(String endpoint, Integer maxRetries, Integer currentRetry, Integer nextRetryTime, long nexRetryTimestamp) {
        this.endpoint = endpoint;
        this.maxRetries = maxRetries;
        this.currentRetry = currentRetry;
        this.nextRetryTime = nextRetryTime;
        this.nextRetryTimestamp = nexRetryTimestamp;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public Integer getMaxRetries() {
        return this.maxRetries;
    }

    public Integer getCurrentRetry() {
        return this.currentRetry;
    }

    public Integer getNextRetryTime() {
        return this.nextRetryTime;
    }

    public long getNextRetryTimestamp() {
        return this.nextRetryTimestamp;
    }
}
