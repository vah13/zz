/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.startup.readers.SecretMap
 *  com.daimler.cebas.system.control.startup.readers.SecretReader
 */
package com.daimler.cebas.system.control.startup.readers;

import com.daimler.cebas.system.control.startup.readers.SecretMap;
import com.daimler.cebas.system.control.startup.readers.SecretReader;
import java.util.Properties;

public class StaticSecretReader
extends SecretReader {
    public static final int MAXIMUM_SECRET_BYTES = 2500;

    protected boolean overwriteHeader() {
        return false;
    }

    protected int maximumSecretSize() {
        return 2500;
    }

    protected SecretMap getSecret() {
        return this.getSecret(SecretReader.class.getClassLoader().getResourceAsStream("daimler_logo.bmp"));
    }

    protected void prepare(Properties properties, SecretMap secret) {
    }
}
