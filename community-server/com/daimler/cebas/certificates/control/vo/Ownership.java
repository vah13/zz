/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.common.control.vo.CEBASResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Base64;

@ApiModel
public class Ownership
extends CEBASResult {
    @ApiModelProperty(dataType="java.lang.String", value="base64 encoded proof", required=true)
    private String ecuProofOfOwnership;

    public Ownership(String errorMessage) {
        super(errorMessage);
    }

    public Ownership(byte[] ecuProofOfOwnership) {
        this.ecuProofOfOwnership = Base64.getEncoder().encodeToString(ecuProofOfOwnership);
    }

    public String getEcuProofOfOwnership() {
        return this.ecuProofOfOwnership;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String toString() {
        return "Ownership{ecuProofOfOwnership=" + this.ecuProofOfOwnership + '}';
    }
}
