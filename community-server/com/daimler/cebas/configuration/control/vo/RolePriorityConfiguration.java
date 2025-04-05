/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.configuration.control.vo;

public class RolePriorityConfiguration {
    private final String id;
    private final String role;

    public RolePriorityConfiguration(String id, String role) {
        this.id = id;
        this.role = role;
    }

    public String getId() {
        return this.id;
    }

    public String getRole() {
        return this.role;
    }
}
