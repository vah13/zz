/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.validation.failure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

public class ValidationState {
    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private boolean valid = true;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return this.valid;
    }
}
