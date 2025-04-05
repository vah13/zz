/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.configuration.control.mdc;

import java.util.Map;
import org.slf4j.MDC;

public class MdcRunnable
implements Runnable {
    private final Runnable delegate;
    private final Map<String, String> mdc;

    public MdcRunnable(Runnable delegate) {
        this.delegate = delegate;
        this.mdc = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        if (this.mdc != null && !this.mdc.isEmpty()) {
            MDC.setContextMap(this.mdc);
        }
        this.delegate.run();
    }
}
