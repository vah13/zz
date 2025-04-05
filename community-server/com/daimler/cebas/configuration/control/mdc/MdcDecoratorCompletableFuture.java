/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.configuration.control.mdc.MdcConsumer
 *  com.daimler.cebas.configuration.control.mdc.MdcFunction
 *  com.daimler.cebas.configuration.control.mdc.MdcRunnable
 *  com.daimler.cebas.configuration.control.mdc.MdcSupplier
 */
package com.daimler.cebas.configuration.control.mdc;

import com.daimler.cebas.configuration.control.mdc.MdcConsumer;
import com.daimler.cebas.configuration.control.mdc.MdcFunction;
import com.daimler.cebas.configuration.control.mdc.MdcRunnable;
import com.daimler.cebas.configuration.control.mdc.MdcSupplier;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MdcDecoratorCompletableFuture<T>
extends CompletableFuture<T> {
    @Override
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        MdcFunction f = new MdcFunction(fn);
        return super.thenApplyAsync((Function)f);
    }

    @Override
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn, Executor executor) {
        MdcFunction f = new MdcFunction(fn);
        return super.thenApplyAsync((Function)f, executor);
    }

    @Override
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> a) {
        MdcConsumer f = new MdcConsumer(a);
        return super.thenAcceptAsync((Consumer)f);
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(new MdcSupplier(supplier));
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync((Runnable)new MdcRunnable(runnable));
    }
}
