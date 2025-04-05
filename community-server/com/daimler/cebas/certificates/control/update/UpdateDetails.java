/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.update.UpdateSteps
 */
package com.daimler.cebas.certificates.control.update;

import com.daimler.cebas.certificates.control.update.UpdateSteps;

public class UpdateDetails {
    private UpdateSteps step;
    private String message;
    private String[] messageArguments;
    private boolean error;

    public UpdateDetails(UpdateSteps step, String message) {
        this.step = step;
        this.message = message;
    }

    public UpdateDetails(UpdateSteps step, String message, String[] messageArguments) {
        this.step = step;
        this.message = message;
        this.messageArguments = messageArguments;
    }

    public UpdateDetails(UpdateSteps step, String message, boolean error) {
        this(step, message);
        this.error = error;
    }

    public String getMessage() {
        return this.message;
    }

    public UpdateSteps getStep() {
        return this.step;
    }

    public boolean isError() {
        return this.error;
    }

    public String[] getMessageArguments() {
        return this.messageArguments;
    }
}
