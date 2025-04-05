/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.ApplicationEvent
 */
package com.daimler.cebas.certificates.control.chain.events;

import org.springframework.context.ApplicationEvent;

public class CertificatesCleanUpEvent
extends ApplicationEvent {
    private static final long serialVersionUID = 376354651775487219L;

    public CertificatesCleanUpEvent(Object source) {
        super(source);
    }
}
