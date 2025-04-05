/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.util.LinkedMultiValueMap
 *  org.springframework.util.MultiValueMap
 */
package com.daimler.cebas.certificates.integration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public final class QueryParamFactory {
    private static final String SKI = "ski";
    private static final String CA_IDENTIFIER = "caId";
    private static final String SOURCE_CA = "sourceCa";
    private static final String TARGET_CA = "targetCa";

    private QueryParamFactory() {
    }

    public static MultiValueMap<String, String> createRootAndBackendIdentifierParam(String preactiveCaId) {
        LinkedMultiValueMap requestMultiValueMap = new LinkedMultiValueMap();
        if (!StringUtils.isNotEmpty(preactiveCaId)) return requestMultiValueMap;
        requestMultiValueMap.add((Object)CA_IDENTIFIER, (Object)preactiveCaId);
        return requestMultiValueMap;
    }

    public static MultiValueMap<String, String> createCertChainParam(String subjectKeyIdentifier) {
        LinkedMultiValueMap requestMultiValueMap = new LinkedMultiValueMap();
        requestMultiValueMap.add((Object)SKI, (Object)subjectKeyIdentifier);
        return requestMultiValueMap;
    }

    public static MultiValueMap<String, String> createLinkCertParam(String sourceCa, String targetCa) {
        LinkedMultiValueMap requestMultiValueMap = new LinkedMultiValueMap();
        if (StringUtils.isNotEmpty(sourceCa)) {
            requestMultiValueMap.add((Object)SOURCE_CA, (Object)sourceCa);
        }
        if (!StringUtils.isNotEmpty(targetCa)) return requestMultiValueMap;
        requestMultiValueMap.add((Object)TARGET_CA, (Object)targetCa);
        return requestMultiValueMap;
    }

    public static MultiValueMap<String, String> createNonVsmIdentifierParam(String backendSKI) {
        LinkedMultiValueMap requestMultiValueMap = new LinkedMultiValueMap();
        requestMultiValueMap.add((Object)CA_IDENTIFIER, (Object)backendSKI);
        return requestMultiValueMap;
    }
}
