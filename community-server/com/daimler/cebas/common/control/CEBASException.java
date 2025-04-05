/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.common.control;

import org.apache.commons.lang3.StringUtils;

public class CEBASException
extends RuntimeException {
    private static final long serialVersionUID = 3182248433901239456L;
    protected String messageId;

    public CEBASException(String message) {
        super(message);
    }

    public CEBASException(String message, String messageId) {
        super(message);
        this.messageId = messageId;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean hasMessageId() {
        return !StringUtils.isEmpty(this.messageId);
    }

    public static boolean hasCEBASExceptionCause(Throwable e) {
        return CEBASException.hasCause(e, CEBASException.class);
    }

    public static boolean hasCause(Throwable e, Class<?> clazz) {
        if (e == null) {
            return false;
        }
        if (!clazz.isAssignableFrom(e.getClass())) return CEBASException.hasCause(e.getCause(), clazz);
        return true;
    }
}
