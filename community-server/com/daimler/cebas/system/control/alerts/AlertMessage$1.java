/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.alerts.AlertMessage
 */
package com.daimler.cebas.system.control.alerts;

import com.daimler.cebas.system.control.alerts.AlertMessage;
import java.util.ArrayDeque;

/*
 * Exception performing whole class analysis ignored.
 */
static final class AlertMessage.1
extends ArrayDeque<String> {
    AlertMessage.1() {
    }

    @Override
    public boolean add(String value) {
        return !this.contains(value) && !AlertMessage.access$000().contains(value) && super.add(value);
    }
}
