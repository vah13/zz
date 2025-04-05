/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.startup.readers.SecretMap
 *  com.daimler.cebas.system.control.startup.readers.SecretReader
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package com.daimler.cebas.system.control.startup.readers;

import com.daimler.cebas.system.control.startup.readers.SecretMap;
import com.daimler.cebas.system.control.startup.readers.SecretReader;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SecretFactory {
    protected static final Log LOG = LogFactory.getLog(SecretFactory.class);
    private Properties properties;
    private SecretMap secret;

    private SecretFactory() {
    }

    public static SecretFactory create(Properties properties) {
        SecretFactory f = new SecretFactory();
        f.properties = properties;
        return f;
    }

    public SecretFactory run(SecretReader s) {
        s.prepare(this.properties, this.secret);
        SecretMap newSecret = s.getSecret();
        this.join(newSecret);
        return this;
    }

    public SecretMap finish() {
        return this.secret;
    }

    private void join(SecretMap newSecret) {
        if (this.secret == null) {
            this.secret = newSecret;
        } else {
            this.secret.join(newSecret);
        }
    }
}
