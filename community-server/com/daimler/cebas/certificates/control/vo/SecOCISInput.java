/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.AbstractSecOCISInput
 *  com.daimler.cebas.certificates.control.vo.ISecOCIsInput
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.AbstractSecOCISInput;
import com.daimler.cebas.certificates.control.vo.ISecOCIsInput;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.StringUtils;

@ApiModel
public class SecOCISInput
extends AbstractSecOCISInput
implements ISecOCIsInput {
    @ApiModelProperty(dataType="java.lang.String", value="The target VIN. The size of the field is 17 characters.", required=true)
    private String targetVIN;

    public SecOCISInput() {
    }

    public SecOCISInput(String ecuCertificate, String backendCertSubjKeyId, String diagCertSerialNumber, String targetECU, String targetVIN) {
        super(ecuCertificate, backendCertSubjKeyId, diagCertSerialNumber, targetECU);
        this.targetVIN = targetVIN;
    }

    public String getTargetVIN() {
        return this.targetVIN;
    }

    @JsonIgnore
    public boolean isInvalid() {
        return StringUtils.isEmpty((Object)this.getBackendCertSubjKeyId()) || StringUtils.isEmpty((Object)this.getEcuCertificate()) || StringUtils.isEmpty((Object)this.getDiagCertSerialNumber()) || StringUtils.isEmpty((Object)this.getTargetECU()) || StringUtils.isEmpty((Object)this.getTargetVIN());
    }
}
