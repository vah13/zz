/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASI18N
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  org.springframework.context.MessageSource
 *  org.springframework.web.context.annotation.RequestScope
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.common.control.CEBASI18N;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import org.springframework.context.MessageSource;
import org.springframework.web.context.annotation.RequestScope;

@CEBASControl
@RequestScope
public class RequestMetadata
extends CEBASI18N {
    private String correlationId = "";

    public RequestMetadata(MessageSource messageSource) {
        super(messageSource);
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }
}
