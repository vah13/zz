/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationState
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.validation.failure;

import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.logs.control.Logger;
import java.util.logging.Level;

public class CSRValidationFailureOutput
implements ValidationFailureOutput {
    private ValidationState csr;
    private ValidationState permission;
    private Logger logger;

    public CSRValidationFailureOutput(ValidationState permission, ValidationState csr, Logger logger) {
        this.csr = csr;
        this.logger = logger;
        this.permission = permission;
    }

    public void outputFailure(CEBASException exception) {
        this.permission.setValid(false);
        this.csr.setValid(false);
        this.logger.log(Level.WARNING, "000317X", "Skipping CSR generation from permission: " + this.permission.toString() + " Reason: " + exception.getMessage(), this.getClass().getSimpleName());
    }
}
