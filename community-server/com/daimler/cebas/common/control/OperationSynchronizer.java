/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed
 *  org.springframework.http.ResponseEntity
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import org.springframework.http.ResponseEntity;

public interface OperationSynchronizer {
    public static final String MODIFICATIONS_NOT_ALLOWED = "Modifications not allowed";
    public static final Semaphore lock = new Semaphore(1);

    default public <T> ResponseEntity<T> synchronizeWithResponseEntity(Supplier<ResponseEntity<T>> execution) {
        try {
            lock.acquire();
            ResponseEntity<T> responseEntity = execution.get();
            return responseEntity;
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StoreModificationNotAllowed(MODIFICATIONS_NOT_ALLOWED);
        }
        finally {
            lock.release();
        }
    }

    default public <T> List<T> synchronizeWithList(Supplier<List<T>> execution) {
        try {
            lock.acquire();
            List<T> list = execution.get();
            return list;
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StoreModificationNotAllowed(MODIFICATIONS_NOT_ALLOWED);
        }
        finally {
            lock.release();
        }
    }

    default public void synchronize(Runnable execution) {
        try {
            lock.acquire();
            execution.run();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StoreModificationNotAllowed(MODIFICATIONS_NOT_ALLOWED);
        }
        finally {
            lock.release();
        }
    }

    default public <T> T synchronize(Supplier<T> execution, T type) {
        try {
            lock.acquire();
            T t = execution.get();
            return t;
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new StoreModificationNotAllowed(MODIFICATIONS_NOT_ALLOWED);
        }
        finally {
            lock.release();
        }
    }
}
