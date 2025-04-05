/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.InitializingBean
 */
package com.daimler.cebas.system.control.websocket;

import org.springframework.beans.factory.InitializingBean;

public abstract class WebsocketAbstractController
implements InitializingBean {
    protected static WebsocketAbstractController instance;

    public abstract void updateAlertMessages();

    public static WebsocketAbstractController getInstance() {
        return instance;
    }
}
