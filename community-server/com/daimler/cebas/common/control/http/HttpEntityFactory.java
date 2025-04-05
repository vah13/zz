/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.HttpHeaderFactory
 *  org.springframework.http.HttpEntity
 *  org.springframework.http.MediaType
 *  org.springframework.util.MultiValueMap
 */
package com.daimler.cebas.common.control.http;

import com.daimler.cebas.common.control.HttpHeaderFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

public final class HttpEntityFactory {
    private HttpEntityFactory() {
    }

    public static HttpEntity buildDefaultPKIRequestEntity(String accessToken) {
        return new HttpEntity((MultiValueMap)HttpHeaderFactory.buildDefaultPKIRequestHeader((String)accessToken, (MediaType)MediaType.TEXT_PLAIN));
    }

    public static HttpEntity buildDefaultPKIRequestEntity(String accessToken, MediaType mediaType, String body) {
        return new HttpEntity((Object)body, (MultiValueMap)HttpHeaderFactory.buildDefaultPKIRequestHeader((String)accessToken, (MediaType)mediaType));
    }

    public static HttpEntity buildDefaultPKIRequestEntityWithNoToken(MediaType mediaType, String body) {
        return new HttpEntity((Object)body, (MultiValueMap)HttpHeaderFactory.buildDefaultPKIRequestHeaderWithNoAuthCode((MediaType)mediaType));
    }
}
