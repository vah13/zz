/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.zkNoSupport;

import com.daimler.cebas.common.control.vo.CEBASResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ZkNoMappingResult
extends CEBASResult {
    @ApiModelProperty(value="The Subject Key Identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes.", required=true)
    private String ski;
    @ApiModelProperty(value="The ZK number.", required=true)
    private String zkNo;

    public ZkNoMappingResult() {
    }

    public ZkNoMappingResult(String ski, String zkNo) {
        this.ski = ski;
        this.zkNo = zkNo;
    }

    public String getSki() {
        return this.ski;
    }

    public String getZkNo() {
        return this.zkNo;
    }
}
