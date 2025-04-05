/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.users.control.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserLoginRequest {
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The user name. Length between 1 and 7 characters. Must be alpha numeric, non blank.")
    private String userName;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The user password as base64 encoded UTF-8 string. Length of the undecoded string must be between 9 and 100 characters. Must contain upper case, lower case, digit, and special characters.")
    private String userPassword;

    public String getUserName() {
        return this.userName;
    }

    public String getUserPassword() {
        return this.userPassword;
    }
}
