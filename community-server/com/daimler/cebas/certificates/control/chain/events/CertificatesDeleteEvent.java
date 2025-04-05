/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.ApplicationEvent
 */
package com.daimler.cebas.certificates.control.chain.events;

import java.util.List;
import org.springframework.context.ApplicationEvent;

public class CertificatesDeleteEvent
extends ApplicationEvent {
    private static final long serialVersionUID = 4303738505537567864L;
    private List<String> ids;

    public CertificatesDeleteEvent(Object source, List<String> ids) {
        super(source);
        this.ids = ids;
    }

    public List<String> getIds() {
        return this.ids;
    }
}
