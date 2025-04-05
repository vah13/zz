/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.users.entity.User
 */
package com.daimler.cebas.users.control.vo;

import com.daimler.cebas.users.entity.User;
import org.apache.commons.lang3.StringUtils;

public class UserData {
    private final String id;
    private final String userName;
    private final String name;

    public UserData(User user) {
        this.userName = user.getUserName();
        this.id = user.getEntityId();
        this.name = StringUtils.defaultString(user.getFirstName()) + " " + StringUtils.defaultString(user.getLastName());
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getUserName() {
        return this.userName;
    }
}
