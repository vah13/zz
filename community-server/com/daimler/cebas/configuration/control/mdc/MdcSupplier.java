/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.web.context.request.RequestAttributes
 *  org.springframework.web.context.request.RequestContextHolder
 */
package com.daimler.cebas.configuration.control.mdc;

import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class MdcSupplier<T>
implements Supplier<T> {
    private final Supplier<T> delegate;
    private final Map<String, String> mdc;
    private final RequestAttributes requestAttributes;

    public MdcSupplier(Supplier<T> delegate) {
        this.delegate = delegate;
        this.mdc = MDC.getCopyOfContextMap();
        this.requestAttributes = RequestContextHolder.getRequestAttributes();
    }

    @Override
    public T get() {
        if (this.mdc != null && !this.mdc.isEmpty()) {
            MDC.setContextMap(this.mdc);
        }
        RequestContextHolder.setRequestAttributes((RequestAttributes)this.requestAttributes);
        return this.delegate.get();
    }
}
