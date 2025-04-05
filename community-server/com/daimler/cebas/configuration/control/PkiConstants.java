/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.configuration.control.PkiUrlProperty
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.configuration.control.PkiUrlProperty;

public class PkiConstants {
    public static final String PKI_TEST_ENVIRONMENT = "TEST";
    public static final String PKI_PROD_ENVIRONMENT = "PROD";
    public static final String PROPERTY_PLACEHOLDER_BUILD = "@";
    public static final String PKI_BASE_URL_REGEX = "@" + PkiUrlProperty.PKI_BASE_URL.getProperty() + "@";

    private PkiConstants() {
    }
}
