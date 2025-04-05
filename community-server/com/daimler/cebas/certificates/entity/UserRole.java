/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.InvalidInputException
 */
package com.daimler.cebas.certificates.entity;

import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
import java.util.HashMap;
import java.util.Map;

public final class UserRole
extends Enum<UserRole> {
    public static final /* enum */ UserRole SUPPLIER;
    public static final /* enum */ UserRole DEVELOPMENT_ENHANCED;
    public static final /* enum */ UserRole PRODUCTION;
    public static final /* enum */ UserRole AS_ENHANCED;
    public static final /* enum */ UserRole AS_STANDARD;
    public static final /* enum */ UserRole AS_BASIC;
    public static final /* enum */ UserRole INTERNAL_DIAGNOSTIC_TEST_TOOL;
    public static final /* enum */ UserRole EPTI_TEST_TOOL;
    public static final /* enum */ UserRole NO_ROLE;
    private String text;
    private static final Map<String, String> map;
    private static final /* synthetic */ UserRole[] $VALUES;

    public static UserRole[] values() {
        return (UserRole[])$VALUES.clone();
    }

    public static UserRole valueOf(String name) {
        return Enum.valueOf(UserRole.class, name);
    }

    private UserRole(String text) {
        this.text = text;
    }

    public static UserRole getUserRoleFromByte(byte value) {
        switch (value) {
            case 1: {
                return SUPPLIER;
            }
            case 2: {
                return DEVELOPMENT_ENHANCED;
            }
            case 3: {
                return PRODUCTION;
            }
            case 4: {
                return AS_ENHANCED;
            }
            case 5: {
                return AS_STANDARD;
            }
            case 6: {
                return AS_BASIC;
            }
            case 7: {
                return INTERNAL_DIAGNOSTIC_TEST_TOOL;
            }
            case 8: {
                return EPTI_TEST_TOOL;
            }
        }
        return NO_ROLE;
    }

    public static UserRole getUserRoleFromText(String value) {
        switch (value) {
            case "Supplier": {
                return SUPPLIER;
            }
            case "Development ENHANCED": {
                return DEVELOPMENT_ENHANCED;
            }
            case "Production": {
                return PRODUCTION;
            }
            case "After-Sales ENHANCED": {
                return AS_ENHANCED;
            }
            case "After-Sales STANDARD": {
                return AS_STANDARD;
            }
            case "After-Sales BASIC": {
                return AS_BASIC;
            }
            case "Internal Diagnostic Test Tool": {
                return INTERNAL_DIAGNOSTIC_TEST_TOOL;
            }
            case "ePTI Test Tool": {
                return EPTI_TEST_TOOL;
            }
        }
        return NO_ROLE;
    }

    public String getText() {
        return this.text;
    }

    public static Map<String, String> getValuesMap() {
        return map;
    }

    public static byte getByteFromText(String text) {
        return Byte.decode((String)map.entrySet().stream().filter(entry -> ((String)entry.getValue()).equals(text)).findAny().orElseThrow(() -> new InvalidInputException("Invalid user role")).getKey());
    }

    static {
        UserRole[] values;
        SUPPLIER = new UserRole("Supplier");
        DEVELOPMENT_ENHANCED = new UserRole("Development ENHANCED");
        PRODUCTION = new UserRole("Production");
        AS_ENHANCED = new UserRole("After-Sales ENHANCED");
        AS_STANDARD = new UserRole("After-Sales STANDARD");
        AS_BASIC = new UserRole("After-Sales BASIC");
        INTERNAL_DIAGNOSTIC_TEST_TOOL = new UserRole("Internal Diagnostic Test Tool");
        EPTI_TEST_TOOL = new UserRole("ePTI Test Tool");
        NO_ROLE = new UserRole("");
        $VALUES = new UserRole[]{SUPPLIER, DEVELOPMENT_ENHANCED, PRODUCTION, AS_ENHANCED, AS_STANDARD, AS_BASIC, INTERNAL_DIAGNOSTIC_TEST_TOOL, EPTI_TEST_TOOL, NO_ROLE};
        map = new HashMap<String, String>();
        UserRole[] userRoleArray = values = UserRole.values();
        int n = userRoleArray.length;
        int n2 = 0;
        while (n2 < n) {
            UserRole userRole = userRoleArray[n2];
            if (userRole != NO_ROLE) {
                map.put(Integer.toString(userRole.ordinal() + 1), userRole.getText());
            }
            ++n2;
        }
    }
}
