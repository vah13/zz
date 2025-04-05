/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.validation;

import java.util.List;

public class SystemIntegrityCheckError {
    private String certificateId;
    private List<String> errorMessages;
    private List<String> messageIds;

    public SystemIntegrityCheckError(String certificateId, List<String> errorMessages, List<String> messageIds) {
        this.certificateId = certificateId;
        this.errorMessages = errorMessages;
        this.messageIds = messageIds;
    }

    public String getCertificateId() {
        return this.certificateId;
    }

    public List<String> getErrorMessages() {
        return this.errorMessages;
    }

    public List<String> getMessageIds() {
        return this.messageIds;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String toString() {
        return "SystemIntegrityCheckError [certificateId=" + this.certificateId + ", errorMessages=" + this.errorMessages + ", messageIds=" + this.messageIds + "]";
    }
}
