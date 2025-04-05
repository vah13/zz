/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.exceptions.ConfigurationCheckException
 *  com.daimler.cebas.system.control.startup.CeBASStartupProperty
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package com.daimler.cebas.system.control.startup.readers;

import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SecretMap {
    private static final Log LOG = LogFactory.getLog(SecretMap.class);
    private final Map<String, String> values = new HashMap<String, String>();

    public void put(CeBASStartupProperty key, String value) {
        this.values.put(key.name(), value);
    }

    public String get(CeBASStartupProperty key) {
        return this.values.get(key.name());
    }

    public boolean contains(CeBASStartupProperty key) {
        return this.values.containsKey(key.name());
    }

    public static SecretMap fromString(String encoded) {
        String decoded = new String(Base64.getDecoder().decode(encoded.trim()), StandardCharsets.UTF_8);
        try {
            Map typedMap = (Map)new ObjectMapper().readValue(decoded, (TypeReference)new /* Unavailable Anonymous Inner Class!! */);
            SecretMap sc = new SecretMap();
            sc.values.putAll(typedMap);
            return sc;
        }
        catch (IOException e) {
            LOG.error((Object)e);
            throw new ConfigurationCheckException("Invalid configuration. Try to reset the AppData Folder. " + e.getMessage());
        }
    }

    public String createString(int maximumSecretSize) {
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(this.values);
        }
        catch (JsonProcessingException e) {
            LOG.error((Object)e);
            throw new ConfigurationCheckException("Invalid configuration. Try to reset the AppData Folder. " + e.getMessage());
        }
        String encoded = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        if (encoded.length() <= maximumSecretSize) return String.format("%1$" + maximumSecretSize + "s", encoded);
        throw new ConfigurationCheckException("Invalid configuration. It is not possible to store the configuration to the secret store.");
    }

    public SecretMap join(SecretMap other) {
        this.values.putAll(other.values);
        return this;
    }
}
