/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.users.control.vo;

public class CurrentUserDetails {
    private String firstName;
    private String lastName;
    private String userName;
    private boolean isDefaultUser;
    private boolean isNewUser;
    private boolean checkLocalPassword;
    private boolean isAuthenticationAgainstBackend;
    private boolean isTransitionValid;

    public CurrentUserDetails(String firstName, String lastName, String userName, boolean isDefaultUser, boolean isNewUser, boolean checkLocalPassword, boolean isAuthenticationAgainstBackend, boolean isTransitionValid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.isDefaultUser = isDefaultUser;
        this.isNewUser = isNewUser;
        this.checkLocalPassword = checkLocalPassword;
        this.isAuthenticationAgainstBackend = isAuthenticationAgainstBackend;
        this.isTransitionValid = isTransitionValid;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean isDefaultUser() {
        return this.isDefaultUser;
    }

    public boolean isNewUser() {
        return this.isNewUser;
    }

    public boolean isCheckLocalPassword() {
        return this.checkLocalPassword;
    }

    public boolean isAuthenticationAgainstBackend() {
        return this.isAuthenticationAgainstBackend;
    }

    public boolean isTransitionValid() {
        return this.isTransitionValid;
    }
}
