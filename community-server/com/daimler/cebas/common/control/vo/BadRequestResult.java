/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.common.control.vo;

public class BadRequestResult {
    private final long timeStamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public BadRequestResult(long timeStamp, int status, String error, String message, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public int getStatus() {
        return this.status;
    }

    public String getError() {
        return this.error;
    }

    public String getMessage() {
        return this.message;
    }

    public String getPath() {
        return this.path;
    }
}
