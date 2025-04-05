/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.core.PropertyDefinerBase
 */
package com.daimler.cebas.system.control.validation;

import ch.qos.logback.core.PropertyDefinerBase;

public class FileOnClasspath
extends PropertyDefinerBase {
    String path;

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPropertyValue() {
        if (!this.resourceExists(FileOnClasspath.class.getClassLoader())) return "false";
        return "true";
    }

    private boolean resourceExists(ClassLoader loader) {
        if (loader == null) return false;
        if (this.path == null) {
            return false;
        }
        if (loader.getResource(this.path) == null) return this.resourceExists(loader.getParent());
        return true;
    }
}
