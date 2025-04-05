/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.configuration.control.mdc;

import java.util.Map;
import java.util.function.Consumer;
import org.slf4j.MDC;

public class MdcConsumer<T>
implements Consumer<T> {
    private final Consumer<T> delegate;
    private final Map<String, String> mdc;

    public MdcConsumer(Consumer<T> delegate) {
        this.delegate = delegate;
        this.mdc = MDC.getCopyOfContextMap();
    }

    @Override
    public void accept(T t) {
        if (this.mdc != null && !this.mdc.isEmpty()) {
            MDC.setContextMap(this.mdc);
        }
        this.delegate.accept(t);
    }
}
