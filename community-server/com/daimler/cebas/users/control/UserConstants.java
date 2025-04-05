/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.users.control;

public class UserConstants {
    public static final String INVALID = "Invalid";
    public static final int _1_CHARACTER = 1;
    public static final int _7_CHARACTERS = 7;
    public static final int _9_CHARACTERS = 9;
    public static final int _10_CHARACTERS = 10;
    public static final int _100_CHARACTERS = 100;
    public static final String REGEX_ALPHA_NUMERIC_NON_BLANK = "[a-zA-Z0-9]+";
    public static final String REGEX_PASS_WORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!#\\$%&\\*@\\-\"'()+,/;:=?_>\\.])[A-Za-z\\_\\d\\W][^\\s]{9,}";
    public static final String ERROR_USERNAME = "ErrorUsername";
    public static final String USER_NAME_NOT_UNIQUE = "UserNameNotUnique";
    public static final String JSON_MESSAGE = "message";
    public static final String JSON_DISPLAY_NAME = "displayName";
    public static final String JSON_ACCOUNT_DELETED = "accountDeleted";
    public static final String IS_DEFAULT_USER = "default";

    private UserConstants() {
    }
}
