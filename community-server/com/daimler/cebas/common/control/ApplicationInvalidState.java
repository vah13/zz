/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.ApplicationEvent
 */
package com.daimler.cebas.common.control;

import org.springframework.context.ApplicationEvent;

public class ApplicationInvalidState
extends ApplicationEvent {
    private static final long serialVersionUID = 418964827069043733L;

    public ApplicationInvalidState(Object source) {
        super(source);
    }
}
