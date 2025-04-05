/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.common.control.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Enablement {
    @ApiModelProperty
    private boolean enable;

    public Enablement() {
    }

    public Enablement(boolean value) {
        this.enable = value;
    }

    public boolean isEnable() {
        return this.enable;
    }
}
