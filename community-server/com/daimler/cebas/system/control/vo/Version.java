/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.system.control.vo;

public class Version {
    private String serverVersion;
    private String apiVersion;
    private String build;
    private String system;

    public Version() {
    }

    public Version(String serverVersion, String apiVersion, String build, String system) {
        this.serverVersion = serverVersion;
        this.apiVersion = apiVersion;
        this.build = build;
        this.system = system;
    }

    public String getServerVersion() {
        return this.serverVersion;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public String getBuild() {
        return this.build;
    }

    public String getSystem() {
        return this.system;
    }
}
