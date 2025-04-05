/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.validation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ValidationError {
    private String errorMessage;
    private String subjectKeyIdentifier;
    private String messageId;
    private String[] messageArgs;

    public ValidationError() {
    }

    public ValidationError(String subjectKeyIdentifier, String errorMessage, String messageId, String[] messageArgs) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.errorMessage = errorMessage;
        this.messageId = messageId;
        this.messageArgs = messageArgs;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String[] getMessageArgs() {
        return this.messageArgs;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals((Object)this, obj, false);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode((Object)this, true);
    }
}
