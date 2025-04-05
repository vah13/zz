/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.users.control.vo.CurrentUserDetails
 */
package com.daimler.cebas.users.control.vo;

import com.daimler.cebas.users.control.vo.CurrentUserDetails;

public class UserDetailsWithSession
extends CurrentUserDetails {
    private long remainingSessionSeconds;

    public UserDetailsWithSession(String firstName, String lastName, String userName, boolean isDefaultUser, boolean isNewUser, boolean checkLocalPassword, boolean isAuthenticationAgainstBackend, boolean isTransitionValid) {
        super(firstName, lastName, userName, isDefaultUser, isNewUser, checkLocalPassword, isAuthenticationAgainstBackend, isTransitionValid);
    }

    public UserDetailsWithSession(String firstName, String lastName, String userName, boolean isDefaultUser, boolean isNewUser, boolean checkLocalPassword, boolean isAuthenticationAgainstBackend, boolean isTransitionValid, long remainingSessionSeconds) {
        super(firstName, lastName, userName, isDefaultUser, isNewUser, checkLocalPassword, isAuthenticationAgainstBackend, isTransitionValid);
        this.remainingSessionSeconds = remainingSessionSeconds;
    }

    public long getRemainingSessionSeconds() {
        return this.remainingSessionSeconds;
    }

    public void setRemainingSessionSeconds(long remainingSessionSeconds) {
        this.remainingSessionSeconds = remainingSessionSeconds;
    }
}
