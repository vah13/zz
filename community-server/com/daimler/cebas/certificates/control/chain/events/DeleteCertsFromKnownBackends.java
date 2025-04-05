/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  org.springframework.context.ApplicationEvent
 */
package com.daimler.cebas.certificates.control.chain.events;

import com.daimler.cebas.certificates.control.vo.ImportResult;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEvent;

public class DeleteCertsFromKnownBackends
extends ApplicationEvent {
    private transient List<ImportResult> rootAndBackendsResult;

    public DeleteCertsFromKnownBackends(List<ImportResult> rootAndBackendsResult, Object source) {
        super(source);
        this.rootAndBackendsResult = rootAndBackendsResult;
    }

    public List<ImportResult> getBackends() {
        return this.rootAndBackendsResult.stream().filter(result -> Objects.nonNull(result.getAuthorityKeyIdentifier())).collect(Collectors.toList());
    }
}
