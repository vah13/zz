/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.configuration.control.mdc;

import java.util.Map;
import java.util.function.Function;
import org.slf4j.MDC;

public class MdcFunction<T, R>
implements Function<T, R> {
    private final Function<T, R> delegate;
    private final Map<String, String> mdc;

    public MdcFunction(Function<T, R> delegate) {
        this.delegate = delegate;
        this.mdc = MDC.getCopyOfContextMap();
    }

    @Override
    public R apply(T t) {
        if (this.mdc == null) return this.delegate.apply(t);
        if (this.mdc.isEmpty()) return this.delegate.apply(t);
        MDC.setContextMap(this.mdc);
        return this.delegate.apply(t);
    }
}
