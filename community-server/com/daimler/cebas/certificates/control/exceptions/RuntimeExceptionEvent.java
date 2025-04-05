/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.ApplicationEvent
 */
package com.daimler.cebas.certificates.control.exceptions;

import org.springframework.context.ApplicationEvent;

public class RuntimeExceptionEvent
extends ApplicationEvent {
    private static final long serialVersionUID = -8559505156832249151L;

    public RuntimeExceptionEvent(Object source) {
        super(source);
    }
}
